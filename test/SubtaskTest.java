import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void testForEqualityOfSubtasksWithAnEqualId() {
        Subtask subtask = new Subtask("Тест подзадачи №1", "№1", 1);
        subtask.setId(2);

        Subtask subtask1 = new Subtask("Тест подзадачи №1", "№1", 1);
        subtask1.setId(2);

        Assertions.assertEquals(subtask, subtask1, "Ошибка объекты неравны");
    }

    @Test
    public void testConflictId() {
        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Тест подзадачи №1", "№1", epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        Subtask subtask1 = new Subtask("Тест подзадачи №2", "№2", epic.getId());
        subtask1.setId(2);

        Assertions.assertEquals(subtask, inMemoryTaskManager.getSubtask(subtask.getId()));
        Assertions.assertNotEquals(subtask1, inMemoryTaskManager.getSubtask(subtask1.getId()));
        Assertions.assertEquals(1, inMemoryTaskManager.getAllSubtasks().size());
    }

    @Test
    public void testImmutabilityFieldsOfEpicAfterTheAddition() {
        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Тест подзадачи №1", "№1", epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        Assertions.assertEquals(subtask.name, inMemoryTaskManager.getSubtask(subtask.getId()).name);
        Assertions.assertEquals(subtask.description, inMemoryTaskManager.getSubtask(subtask.getId()).description);
    }
}