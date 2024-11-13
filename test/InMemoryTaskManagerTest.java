import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}