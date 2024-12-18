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

public class HttpTaskManagerSubtasksTest {

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
    public void testAddSubtaskToEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "1");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Test subtask",
                epic.getId(), LocalDateTime.now(), 15);
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Subtask не был успешно добавлен");
        assertEquals(1, epic.subtaskIds.size(), "Subtask не был добавлен в Epic");
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Test epic creation");
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "1",
                epic.getId(), LocalDateTime.now(), 15);
        Subtask subtask2 = new Subtask("Subtask 2", "2",
                epic.getId(), LocalDateTime.now().plusHours(2), 15);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении Subtasks");

        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(2, subtasks.length, "Некорректное количество Subtasks");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Test epic creation");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Test subtask",
                epic.getId(), LocalDateTime.now(), 15);
        manager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении Subtask по ID");
        Subtask returnedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals("Subtask 1", returnedSubtask.getName(), "Имя Subtask некорректно");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Test epic creation");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask to delete", "Testing delete",
                epic.getId(), LocalDateTime.now(), 15);
        manager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при удалении Subtask");

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertTrue(subtasks.isEmpty(), "Subtask не был удален");
    }
}

