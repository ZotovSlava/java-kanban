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

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerHistoryTest {

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
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Test epic creation");
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "1",
                epic.getId(), LocalDateTime.now(), 15);
        Subtask subtask2 = new Subtask("Subtask 2", "2",
                epic.getId(), LocalDateTime.now().plusHours(2), 15);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.getEpic(epic.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении TaskSet");

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(3, tasks.length, "Некорректное количество tasks");

        assertEquals("Epic 1", tasks[2].getName(), "Некорректное имя задачи");
        assertEquals("Subtask 1", tasks[1].getName(), "Некорректное имя задачи");
        assertEquals("Subtask 2", tasks[0].getName(), "Некорректное имя задачи");
    }
}
