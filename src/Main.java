

public class Main {

    public static void main(String[] args) {


        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultManager();

        Task task1 = new Task("Таск1", "Необходимо приготовить 2500 руб.");
        taskManager.createTask(task1);
        Task task2 = new Task("Таск2", "Нужно постирать до приезда девушки !!!");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик1", "Готовимся к дню рождения.");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск1", "Нужно будет кинуть его в лицо именнику.", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск2", "Для настоящего веселья.", epic1.getId());
        taskManager.createSubtask(subtask2);


        taskManager.getTask(task1.getId());
        taskManager.removeTask(task1.getId());
        System.out.println(managers.getDefaultHistory().getHistory());

        taskManager.createTask(task1);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        System.out.println();
        System.out.println(managers.getDefaultHistory().getHistory());
        System.out.println();
        System.out.println();

        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        System.out.println(managers.getDefaultHistory().getHistory());
        System.out.println();
        System.out.println();

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getSubtask(subtask1.getId());
        System.out.println(managers.getDefaultHistory().getHistory());
        System.out.println();
        System.out.println();

        taskManager.removeEpic(epic1.getId());
        System.out.println(managers.getDefaultHistory().getHistory());

    }
}

