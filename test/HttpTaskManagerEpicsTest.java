import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerEpicsTest {

    private HttpTaskServer taskServer;
    private TaskManager manager;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    public void BeforeEach() throws IOException {

        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        client = HttpClient.newHttpClient();
        taskServer.start();
        manager.cleanAllTasks();
        manager.cleanAllEpic();
    }

    @AfterEach
    public void AfterEach() {
        manager.cleanAllTasks();
        manager.cleanAllEpic();
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Test epic creation");
        String epicJson = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Epic не был успешно добавлен");

        List<Epic> epics = manager.getAllEpics();
        assertEquals(1, epics.size(), "Некорректное количество эпиков в менеджере");
        assertEquals("Epic 1", epics.getFirst().getName(), "Имя эпика некорректно");
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "1");
        Epic epic2 = new Epic("Epic 2", "2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении эпиков");

        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(2, epics.length, "Некорректное количество эпиков");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "1");
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении эпика по ID");
        Epic returnedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals("Epic 1", returnedEpic.getName(), "Имя эпика некорректно");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to delete", "Testing delete");
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при удалении эпика");

        List<Epic> epics = manager.getAllEpics();
        assertTrue(epics.isEmpty(), "Epic не был удален");
    }
}
