import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public EpicsHandler(TaskManager taskManager) {
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
                    List<Epic> epics = taskManager.getAllEpics();
                    String jsonResponse = gson.toJson(epics);
                    sendText(httpExchange, jsonResponse);

                } else if (splitString.length == 3) {
                    Epic epic = taskManager.getEpic(Integer.parseInt(splitString[2]));

                    if (epic != null) {
                        String jsonResponse = gson.toJson(epic);
                        sendText(httpExchange, jsonResponse);
                    } else {
                        sendNotFound(httpExchange);
                    }
                } else if (splitString.length == 4) {
                    List<Subtask> subtasks = taskManager.getEpicSubtasks(Integer.parseInt(splitString[2]));
                    if (!subtasks.isEmpty()) {
                        String jsonResponse = gson.toJson(subtasks);
                        sendText(httpExchange, jsonResponse);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;

            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                epic.setType(TaskType.EPIC);
                epic.subtaskIds = new ArrayList<Integer>();
                if (epic.getStatus() == null) {
                    epic.setStatus(TaskStatus.NEW);
                }
                taskManager.createEpic(epic);
                sendText(httpExchange);
                break;

            case "DELETE":
                Epic epicToRemove = taskManager.getEpic(Integer.parseInt(splitString[2]));
                String jsonResponse = gson.toJson(epicToRemove);
                taskManager.removeEpic(Integer.parseInt(splitString[2]));
                sendText(httpExchange, jsonResponse);
                break;

            default:
                sendInternalServerError(httpExchange);
        }
    }
}
