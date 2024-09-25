public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Оплатить жилье", "Необходимо приготовить 2500 руб.");
        taskManager.createTask(task1);
        Task task2 = new Task("Постирать вещи", "Нужно постирать до приезда девушки !!!");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Купить продукты", "Готовимся к дню рождения.");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("1.Торт", "Нужно будет кинуть его в лицо именнику.", epic1.id);
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("2.Водка", "Для настоящего веселья.", epic1.id);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Приготовиться к поездке", "Наконец-то отдых!!!");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("1.Собрать чемодан", "Не забыть положить таблетки.", epic2.id);
        taskManager.createSubtask(subtask3);

        taskManager.getAllTasks();

        System.out.println();
        System.out.println();

        taskManager.getAllEpics();

        System.out.println();
        System.out.println();

        taskManager.getAllSubtasks();

        System.out.println();
        System.out.println();

        taskManager.changeTaskStatus(TaskStatus.IN_PROGRESS, task1);

        taskManager.changeTaskStatus(TaskStatus.DONE, task2);

        taskManager.changeSubtaskStatus(TaskStatus.IN_PROGRESS, subtask3);

        taskManager.changeSubtaskStatus(TaskStatus.DONE, subtask1);

        taskManager.changeSubtaskStatus(TaskStatus.DONE, subtask2);

        System.out.println("Статус task1 должен быть IN_PROGRESS " + task1.status);
        System.out.println();
        System.out.println();
        System.out.println("Статус task2 должен быть DONE " + task2.status);
        System.out.println();
        System.out.println();
        System.out.println("Статус epic1 должен быть DONE " + epic1.status);
        System.out.println();
        System.out.println();
        System.out.println("Статус epic2 должен быть IN_PROGRESS " + epic2.status);

        taskManager.removeEpic(epic2.id);

        System.out.println();
        System.out.println();

        taskManager.getAllEpics();

        System.out.println();
        System.out.println();

        taskManager.getAllSubtasks();
    }
}
