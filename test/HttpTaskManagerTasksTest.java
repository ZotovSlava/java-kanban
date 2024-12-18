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

public class HttpTaskManagerTasksTest {

    private HttpTaskServer taskServer;
    private TaskManager manager;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    public void BeforeEach() throws IOException {
        manager = Managers.getDefaultManager();
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "1", LocalDateTime.now(), 15);
        String taskJson = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ошибка при добавлении задачи");

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size(), "Некорректное количество задач в менеджере");
        assertEquals("Test Task", tasks.getFirst().getName(), "Некорректное имя задачи");

        Task task1 = new Task("Test Task", "1", LocalDateTime.now(), 15);
        String taskJson1 = gson.toJson(task1);

        URI url1 = URI.create("http://localhost:8080/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response1.statusCode(), "Ошибка при проверке задачи на пересечение");
        assertEquals(1, tasks.size(), "Некорректное количество задач в менеджере");
        assertEquals("Test Task", tasks.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {

        LocalDateTime timeForTask = LocalDateTime.of(2025, 1, 2, 1,0);
        LocalDateTime timeForTask1 = LocalDateTime.of(2025, 2, 2, 1,0);

        Task task1 = new Task("Task 1", "1", timeForTask1 , 15);
        Task task2 = new Task("Task 2", "2", timeForTask,15);

        manager.createTask(task1);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при получении задач");
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length, "Некорректное количество задач");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        LocalDateTime timeForTask2 = LocalDateTime.of(2026, 3, 2, 1,0);
        Task task = new Task("Task to delete", "Testing delete",timeForTask2,50);
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ошибка при удалении задачи");
        assertTrue(manager.getAllTasks().isEmpty(), "Задача не была удалена");
    }
}




