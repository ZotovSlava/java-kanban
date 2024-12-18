import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String[] splitString = path.split("/");

        switch (method) {
            case "GET":
                if (splitString.length == 2) {
                    List<Task> tasks = taskManager.getAllTasks();
                    String jsonResponse = gson.toJson(tasks);
                    sendText(httpExchange, jsonResponse);

                } else if (splitString.length == 3) {
                    Task task = taskManager.getTask(Integer.parseInt(splitString[2]));

                    if (task != null) {
                        String jsonResponse = gson.toJson(task);
                        sendText(httpExchange, jsonResponse);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;

            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                task.setType(TaskType.TASK);
                if (task.getStatus() == null) {
                    task.setStatus(TaskStatus.NEW);
                }

                if (splitString.length == 2) {

                    if (taskManager.createTask(task)) {
                        sendText(httpExchange);
                    } else {
                        sendHasInteractions(httpExchange);
                    }
                } else if (splitString.length == 3) {

                    if (taskManager.updateTask(task)) {
                        sendText(httpExchange);
                    } else {
                        sendHasInteractions(httpExchange);
                    }
                }
                break;

            case "DELETE":
                Task taskToRemove = taskManager.getTask(Integer.parseInt(splitString[2]));
                String jsonResponse = gson.toJson(taskToRemove);
                taskManager.removeTask(Integer.parseInt(splitString[2]));
                sendText(httpExchange, jsonResponse);
                break;

            default:
                sendInternalServerError(httpExchange);
        }
    }
}
