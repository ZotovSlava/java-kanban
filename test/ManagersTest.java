import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefaultManager();
    InMemoryHistoryManager inMemoryHistoryManager = managers.getDefaultHistory();

    @Test
    void taskManagerShouldNotBeNull() {
        Assertions.assertNotNull(taskManager);
    }

    @Test
    void inMemoryHistoryManagerShouldNotBeNull() {
        Assertions.assertNotNull(inMemoryHistoryManager);
    }
}