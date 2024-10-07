import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    public void testForEqualityOfEpicsWithAnEqualId() {
        Epic epic = new Epic("Тест задача №1", "№1");
        epic.setId(1);
        Epic epic1 = new Epic("Тест задача №1", "№1");
        epic1.setId(1);

        Assertions.assertEquals(epic, epic1, "Ошибка объекты неравны");
    }

    @Test
    public void testConflictId() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);
        Epic epic1 = new Epic("Тест задача №2", "№2");
        epic1.setId(1);

        Assertions.assertEquals(epic, inMemoryTaskManager.getEpic(epic.getId()));
        Assertions.assertNotEquals(epic1, inMemoryTaskManager.getEpic(epic1.getId()));
        Assertions.assertEquals(1, inMemoryTaskManager.getAllEpics().size());
    }

    @Test
    public void testImmutabilityFieldsOfEpicAfterTheAddition() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic = new Epic("Тест задача №1", "№1");
        inMemoryTaskManager.createEpic(epic);

        Assertions.assertEquals(epic.name, inMemoryTaskManager.getEpic(epic.getId()).name);
        Assertions.assertEquals(epic.description, inMemoryTaskManager.getEpic(epic.getId()).description);
    }
}