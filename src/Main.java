import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {


        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultManager();

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

        taskManager.getEpic(epic1.id);
        taskManager.getEpic(epic2.id);
        taskManager.getEpic(epic2.id);
        taskManager.getTask(task1.id);
        taskManager.getTask(task2.id);

        taskManager.getSubtask(subtask1.id);
        taskManager.getSubtask(subtask2.id);
        taskManager.getSubtask(subtask3.id);
        taskManager.getSubtask(subtask3.id);
        taskManager.getSubtask(subtask3.id);
        taskManager.getSubtask(subtask3.id);
        Subtask subtask4 = new Subtask("2.Водка", "Для настоящего веселья.", epic1.getId());
        subtask4.setId(subtask2.getId());
        subtask4.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask4);
        taskManager.getEpic(epic1.id);

        System.out.println(managers.getDefaultHistory().getHistory());
    }
}

