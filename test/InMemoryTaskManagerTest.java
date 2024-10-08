import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void addNewTaskAndGetItBackAndDeleteIt() {
        Task task1 = new Task("Test Task №1", "№1");
        taskManager.createTask(task1);

        Assertions.assertEquals(task1, taskManager.getTask(task1.getId()), "Ошибка при добавлении задачи");
        Assertions.assertEquals(1, taskManager.getAllTasks().size(),
                "Кол-во сохраненных задач несоответствует ожиданию");

        taskManager.removeTask(task1.getId());
        Assertions.assertNull(taskManager.getTask(task1.getId()), "Ошибка при удалении задачи");
    }

    @Test
    public void addNewEpicAndGetItBackAndDeleteIt() {
        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        Assertions.assertEquals(epic1, taskManager.getEpic(epic1.getId()), "Ошибка при добавлении эпика");
        Assertions.assertEquals(1, taskManager.getAllEpics().size(),
                "Кол-во сохраненных эпиков несоответствует ожиданию");

        taskManager.removeEpic(epic1.getId());
        Assertions.assertNull(taskManager.getEpic(epic1.getId()), "Ошибка при удалении задачи");
    }

    @Test
    public void addNewSubtaskAndGetItBackAndDeleteIt() {

        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1", epic1.getId());
        taskManager.createSubtask(subtask1);

        Assertions.assertEquals(subtask1, taskManager.getSubtask(subtask1.getId()),
                "Ошибка при добавлении подзадачи");
        Assertions.assertEquals(1, taskManager.getAllSubtasks().size(),
                "Кол-во сохраненных подзадач несоответствует ожиданию");

        taskManager.removeSubtask(subtask1.getId());
        Assertions.assertNull(taskManager.getSubtask(subtask1.getId()), "Ошибка при удалении задачи");
    }

    @Test
    public void shouldBe2SubtaskIn1EpicAndShouldBeDeleteSubtaskIfEpicDelete() {
        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1", epic1.getId());
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1", epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Assertions.assertEquals(2, epic1.subtaskIds.size());

        taskManager.removeEpic(epic1.getId());
        Assertions.assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    public void epicShouldChangeStatusIfSubtasksChangeStatus() {

        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1", epic1.getId());
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1", epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask1.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask1.getStatus());

        Subtask subtask3 = new Subtask("Test Subtask №1up", "Belongs Epic №1", epic1.getId());
        subtask3.setId(subtask1.getId());
        subtask3.setStatus(TaskStatus.DONE);

        taskManager.updateSubtask(subtask3);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask3.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask2.getStatus());

        Subtask subtask4 = new Subtask("Test Subtask №2up", "Belongs Epic №1", epic1.getId());
        subtask4.setId(subtask2.getId());
        subtask4.setStatus(TaskStatus.DONE);

        taskManager.updateSubtask(subtask4);

        Assertions.assertEquals(TaskStatus.DONE, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask3.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask4.getStatus());

        taskManager.removeSubtask(subtask3.getId());
        taskManager.removeSubtask(subtask4.getId());

        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());
    }

    @Test
    public void Last10CalledTasksShouldBeRecordedInHistoryAndReturnAbout() {
        Task task1 = new Task("Задача №1", "№1");
        Task task2 = new Task("Задача №2", "№2");
        Task task3 = new Task("Задача №3", "№3");
        Task task4 = new Task("Задача №4", "№4");
        Task task5 = new Task("Задача №5", "№5");
        Task task6 = new Task("Задача №6", "№6");
        Task task7 = new Task("Задача №7", "№7");
        Task task8 = new Task("Задача №8", "№8");
        Task task9 = new Task("Задача №9", "№9");
        Task task10 = new Task("Задача №10", "№10");
        Task task11 = new Task("Задача №11", "№11");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);
        taskManager.createTask(task7);
        taskManager.createTask(task8);
        taskManager.createTask(task9);
        taskManager.createTask(task10);
        taskManager.createTask(task11);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.getTask(task4.getId());
        taskManager.getTask(task5.getId());
        taskManager.getTask(task6.getId());
        taskManager.getTask(task7.getId());
        taskManager.getTask(task8.getId());
        taskManager.getTask(task9.getId());
        taskManager.getTask(task10.getId());
        taskManager.getTask(task11.getId());


        List<Task> historyList = taskManager.getHistory();

        Assertions.assertEquals(task2,historyList.get(0));
        Assertions.assertEquals(task3,historyList.get(1));
        Assertions.assertEquals(task4,historyList.get(2));
        Assertions.assertEquals(task5,historyList.get(3));
        Assertions.assertEquals(task6,historyList.get(4));
        Assertions.assertEquals(task7,historyList.get(5));
        Assertions.assertEquals(task8,historyList.get(6));
        Assertions.assertEquals(task9,historyList.get(7));
        Assertions.assertEquals(task10,historyList.get(8));
        Assertions.assertEquals(task11,historyList.get(9));
    }
}