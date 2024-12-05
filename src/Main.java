import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultManager();


        Task task1 = new Task("Таск №1", "Описание №1"); //Задача без времени
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

        System.out.println(taskManager.getPrioritizedTasks());

        Epic epic2 = new Epic("Эпик1 up", "Оп. Эпик1 up");
        epic2.setId(epic1.getId());
        epic2.setStartTime(epic1.getStartTime());
        epic2.setDuration(epic1.getDuration());
        taskManager.updateEpic(epic2);

        System.out.println(taskManager.getPrioritizedTasks());
    }
}

