import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void testForEqualityOfTasksWithAnEqualId() {
        Task task = new Task("Тест задача №1", "№1");
        task.setId(1);

        Task task1 = new Task("Тест задача №1", "№1");
        task1.setId(1);

        Assertions.assertEquals(task, task1, "Ошибка объекты неравны");
    }

    @Test
    public void testConflictId() {
        Task task = new Task("Тест задача №1", "№1");
        inMemoryTaskManager.createTask(task);

        Task task1 = new Task("Тест задача №2", "№2");
        task1.setId(1);

        Assertions.assertEquals(task, inMemoryTaskManager.getTask(task.getId()));
        Assertions.assertNotEquals(task1, inMemoryTaskManager.getTask(task1.getId()));
        Assertions.assertEquals(1, inMemoryTaskManager.getAllTasks().size());
    }

    @Test
    public void testImmutabilityFieldsOfTaskAfterTheAddition() {
        Task task = new Task("Тест задача №1", "№1");
        inMemoryTaskManager.createTask(task);

        Assertions.assertEquals(task.name, inMemoryTaskManager.getTask(task.getId()).name);
        Assertions.assertEquals(task.description, inMemoryTaskManager.getTask(task.getId()).description);
    }
}