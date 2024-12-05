import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void testCreateAndGetTask() {
        super.testCreateAndGetTask();
    }

    @Test
    void testRemoveTask() {
        super.testRemoveTask();
    }

    @Test
    void testGetALLTaskAndCleanAllTask() {
        super.testGetALLTaskAndCleanAllTask();
    }

    @Test
    void getEpicSubtasks() {
        super.getEpicSubtasks();
    }

    @Test
    void testEpicStatusCalculation() {
        super.testEpicStatusCalculation();
    }

    @Test
    void testTaskTimeOverlap() {
        super.testTaskTimeOverlap();
    }

    @Test
    void testCalculateEpicsTime() {
        super.testCalculateEpicsTime();
    }
}