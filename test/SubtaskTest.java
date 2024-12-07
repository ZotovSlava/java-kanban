import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class SubtaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void testForEqualityOfSubtasksWithAnEqualId() {
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 1, 30);

        Subtask subtask = new Subtask("Тест подзадачи №1", "№1",
                1, timeForSubtask1, 100);
        subtask.setId(2);

        Subtask subtask1 = new Subtask("Тест подзадачи №1", "№1", 1,
                timeForSubtask2, 100);
        subtask1.setId(2);

        Assertions.assertEquals(subtask, subtask1, "Ошибка объекты неравны");
    }


    @Test
    public void testConflictId() {
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 1, 30);

        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Тест подзадачи №1", "№1",
                epic.getId(), timeForSubtask1, 100);
        inMemoryTaskManager.createSubtask(subtask);

        Subtask subtask1 = new Subtask("Тест подзадачи №2", "№2",
                epic.getId(), timeForSubtask2, 50);
        subtask1.setId(2);

        Assertions.assertEquals(subtask, inMemoryTaskManager.getSubtask(subtask.getId()));
        Assertions.assertNotEquals(subtask1, inMemoryTaskManager.getSubtask(subtask1.getId()));
        Assertions.assertEquals(1, inMemoryTaskManager.getAllSubtasks().size());

        inMemoryTaskManager.cleanAllTasks();
        inMemoryTaskManager.cleanAllEpic();
    }

    @Test
    public void testImmutabilityFieldsOfEpicAfterTheAddition() {
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Тест подзадачи №1", "№1",
                epic.getId(), timeForSubtask1, 60);
        inMemoryTaskManager.createSubtask(subtask);

        Assertions.assertEquals(subtask.name, inMemoryTaskManager.getSubtask(subtask.getId()).name);
        Assertions.assertEquals(subtask.description, inMemoryTaskManager.getSubtask(subtask.getId()).description);

        inMemoryTaskManager.cleanAllTasks();
        inMemoryTaskManager.cleanAllEpic();
    }
}