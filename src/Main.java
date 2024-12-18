import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
       Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        TaskManager taskManager = managers.getDefaultManager();
        LocalDateTime timeForSubtask3 = LocalDateTime.of(2025, 1, 1, 0,0);


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






        /*
        //Задача без времени
        taskManager.createTask(task1);


        LocalDateTime timeForSubtask0 = LocalDateTime.of(2025, 1, 1, 0,0);
        Task task2 = new Task("Таск №2", "Описание №2", timeForSubtask0, 30); // Задача со временем
        taskManager.createTask(task2);

        System.out.println(taskManager.getPrioritizedTasks());

        LocalDateTime timeForSubtask3 = LocalDateTime.of(2025, 1, 1, 0,0);
        Task task3 = new Task("Таск №3", "Описание №3", timeForSubtask3, 30); // Задача с перечечением времени
        taskManager.createTask(task3);

        System.out.println(taskManager.getPrioritizedTasks());

        Task task4 = new Task("Таск №2", "Описание №2"); // Задача c удаленеим времени
        task4.setId(task2.getId());
        taskManager.updateTask(task4);

        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println();
        System.out.println();



        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 2, 1,0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 0,0);

        Epic epic1 = new Epic("Эпик1", "Оп. Эпик1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаск1", "Оп. Сабтаск1",
                epic1.getId(), timeForSubtask1, 50);
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск2", "Оп. Сабтаск2",
                epic1.getId(),timeForSubtask2, 30);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаск2", "Оп. Сабтаск2",
                epic1.getId(),LocalDateTime.now(), 100);
        taskManager.createSubtask(subtask3);

        System.out.println(taskManager.getPrioritizedTasks());

        taskManager.removeSubtask(subtask3.getId());

        System.out.println(taskManager.getPrioritizedTasks());

        Epic epic2 = new Epic("Эпик1 up", "Оп. Эпик1 up");
        epic2.setId(epic1.getId());
        epic2.setStartTime(epic1.getStartTime());
        epic2.setDuration(epic1.getDuration());
        taskManager.updateEpic(epic2);

        System.out.println(taskManager.getPrioritizedTasks());

        taskManager.removeSubtask(subtask2.getId());
        taskManager.removeSubtask(subtask1.getId());

        System.out.println(taskManager.getPrioritizedTasks());

         */
    }
}

