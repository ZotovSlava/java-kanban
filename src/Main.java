import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        TaskManager taskManager = managers.getDefaultManager();
        LocalDateTime timeForSubtask3 = LocalDateTime.of(2025, 1, 1, 0, 0);


        String json1 = """
                   {
                     "name": "epic 1",
                     "description": "Description 1"
                   }
                """;
        Epic epic = gson.fromJson(json1, Epic.class);
        epic.setType(TaskType.EPIC);
        if (epic.getStatus() == null) {
            epic.setStatus(TaskStatus.NEW);

            taskManager.createEpic(epic);

            String json = """
                            {
                              "name": "subtask 1",
                              "description": "SubDes 1",
                              "epicLinkId": 1,
                              "startTime": "2024.01.01 10:00",
                              "duration": 30
                            }
                    """;


            Subtask subtask = gson.fromJson(json, Subtask.class);
            subtask.setType(TaskType.SUBTASK);
            if (subtask.getStatus() == null) {
                subtask.setStatus(TaskStatus.NEW);
            }


            taskManager.createSubtask(subtask);

            System.out.println(taskManager.getPrioritizedTasks());
        }
    }
}

