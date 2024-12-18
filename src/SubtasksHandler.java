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

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public SubtasksHandler(TaskManager taskManager) {
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
                    List<Subtask> subtasks = taskManager.getAllSubtasks();
                    String jsonResponse = gson.toJson(subtasks);
                    sendText(httpExchange, jsonResponse);

                } else if (splitString.length == 3) {
                    Subtask subtask = taskManager.getSubtask(Integer.parseInt(splitString[2]));

                    if (subtask != null) {
                        String jsonResponse = gson.toJson(subtask);
                        sendText(httpExchange, jsonResponse);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;

            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);
                subtask.setType(TaskType.SUBTASK);
                if (subtask.getStatus() == null) {
                    subtask.setStatus(TaskStatus.NEW);
                }

                if (splitString.length == 2) {

                    if (taskManager.createSubtask(subtask)) {
                        sendText(httpExchange);
                    } else {
                        sendHasInteractions(httpExchange);
                    }
                } else if (splitString.length == 3) {

                    if (taskManager.updateSubtask(subtask)) {
                        sendText(httpExchange);
                    } else {
                        sendHasInteractions(httpExchange);
                    }
                }
                break;

            case "DELETE":
                Subtask subtaskToRemove = taskManager.getSubtask(Integer.parseInt(splitString[2]));
                String jsonResponse = gson.toJson(subtaskToRemove);
                taskManager.removeSubtask(Integer.parseInt(splitString[2]));
                sendText(httpExchange, jsonResponse);
                break;

            default:
                sendInternalServerError(httpExchange);
        }
    }
}
