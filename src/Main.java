import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Оплатить жилье", "Необходимо приготовить 2500 руб.");
        taskManager.createTask(task1);
        Task task2 = new Task("Постирать вещи", "Нужно постирать до приезда девушки !!!");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Купить продукты", "Готовимся к дню рождения.");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("1.Торт", "Нужно будет кинуть его в лицо именнику.", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("2.Водка", "Для настоящего веселья.", epic1.getId());
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Приготовиться к поездке", "Наконец-то отдых!!!");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("1.Собрать чемодан", "Не забыть положить таблетки.", epic2.getId());
        taskManager.createSubtask(subtask3);

        System.out.println(taskManager.getAllTasks());

        System.out.println();
        System.out.println();

        System.out.println(taskManager.getAllEpics());

        System.out.println();
        System.out.println();

        System.out.println(taskManager.getAllSubtasks());

        System.out.println();
        System.out.println();

        Subtask subtask4 = new Subtask("1.Собрать чемодан", "Не забыть положить таблетки.", epic2.getId());
        subtask4.setStatus(TaskStatus.DONE);
        subtask4.setId(subtask3.getId());

        taskManager.updateSubtask(subtask4);

        System.out.println(epic2.getStatus());


        Subtask subtask5 = new Subtask("2.Водка", "Для настоящего веселья.", epic1.getId());
        subtask5.setStatus(TaskStatus.IN_PROGRESS);
        subtask5.setId(subtask2.getId());

        taskManager.updateSubtask(subtask5);

        System.out.println(epic1.getStatus());

        System.out.println(taskManager.getAllSubtasks());

        taskManager.removeSubtask(subtask2.getId());

        System.out.println(epic1.getStatus());

        System.out.println(taskManager.getAllSubtasks());

        taskManager.cleanAllSubtask();

        System.out.println(taskManager.getAllEpics());

    }
}
