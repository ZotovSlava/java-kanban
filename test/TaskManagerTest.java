import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    void testCreateAndGetTask() {
        LocalDateTime timeForTask = LocalDateTime.of(2026, 1, 1, 0, 0);
        Task task = new Task("Task 1", "Description 1", timeForTask, 60);
        taskManager.createTask(task);
        Task retrievedTask = taskManager.getTask(task.getId());
        assertNotNull(retrievedTask);
        assertEquals("Task 1", retrievedTask.name);

        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(retrievedEpic);
        assertEquals("Epic 1", retrievedEpic.name);

        LocalDateTime timeForSubtask = LocalDateTime.of(2027, 10, 1, 0, 0);
        Subtask subtask = new Subtask("Subtask 1", "Description 1",
                epic.getId(), timeForSubtask, 100);
        taskManager.createSubtask(subtask);
        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(retrievedSubtask);
        assertEquals("Subtask 1", retrievedSubtask.name);
        assertEquals(epic.getId(), retrievedSubtask.getEpicLinkId());

        taskManager.cleanAllTasks();
        taskManager.cleanAllEpic();
    }

    @Test
    void testRemoveTask() {
        LocalDateTime timeForTask = LocalDateTime.of(2026, 1, 1, 0, 0);
        Task task = new Task("Task 1", "Description 1", timeForTask, 60);
        taskManager.createTask(task);
        taskManager.removeTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));

        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);


        LocalDateTime timeForSubtask = LocalDateTime.of(2027, 7, 1, 0, 0);
        Subtask subtask = new Subtask("Subtask 1", "Description 1",
                epic.getId(), timeForSubtask, 100);
        taskManager.createSubtask(subtask);
        taskManager.removeSubtask(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()));

        taskManager.removeEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()));

        taskManager.cleanAllTasks();
        taskManager.cleanAllEpic();
    }

    @Test
    void testGetALLTaskAndCleanAllTask() {
        LocalDateTime timeForTask = LocalDateTime.of(2026, 1, 3, 0, 0);
        Task task = new Task("Task 1", "Description 1", timeForTask, 60);
        taskManager.createTask(task);

        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 3, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 4, 1, 30);
        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1",
                epic1.getId(), timeForSubtask1, 15);
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1",
                epic1.getId(), timeForSubtask2, 30);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Assertions.assertEquals(1, taskManager.getAllTasks().size());
        Assertions.assertEquals(1, taskManager.getAllEpics().size());
        Assertions.assertEquals(2, taskManager.getAllSubtasks().size());

        taskManager.cleanAllTasks();
        Assertions.assertEquals(0, taskManager.getAllTasks().size());

        taskManager.cleanAllEpic();
        Assertions.assertEquals(0, taskManager.getAllEpics().size());
        Assertions.assertEquals(0, taskManager.getAllSubtasks().size());
    }

    @Test
    void getEpicSubtasks() {
        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 3, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 4, 1, 30);
        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1",
                epic1.getId(), timeForSubtask1, 15);
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1",
                epic1.getId(), timeForSubtask2, 30);
        taskManager.createSubtask(subtask1);
        Assertions.assertEquals(1, epic1.subtaskIds.size());
        taskManager.createSubtask(subtask2);
        Assertions.assertEquals(2, epic1.subtaskIds.size());

        taskManager.cleanAllSubtask();
        Assertions.assertEquals(0, epic1.subtaskIds.size());
        taskManager.cleanAllEpic();
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 1, 30);
        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1",
                epic1.getId(), timeForSubtask1, 15);
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1",
                epic1.getId(), timeForSubtask2, 30);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask1.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask1.getStatus());

        Subtask subtask3 = new Subtask("Test Subtask №1up", "Belongs Epic №1",
                epic1.getId(), timeForSubtask1, 15);
        subtask3.setId(subtask1.getId());
        subtask3.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask3);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask3.getStatus());
        Assertions.assertEquals(TaskStatus.NEW, subtask2.getStatus());

        Subtask subtask4 = new Subtask("Test Subtask №2up", "Belongs Epic №1",
                epic1.getId(), timeForSubtask2, 30);
        subtask4.setId(subtask2.getId());
        subtask4.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask4);
        Assertions.assertEquals(TaskStatus.DONE, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask3.getStatus());
        Assertions.assertEquals(TaskStatus.DONE, subtask4.getStatus());
        taskManager.removeSubtask(subtask3.getId());
        taskManager.removeSubtask(subtask4.getId());
        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());

        LocalDateTime timeForSubtask5 = LocalDateTime.of(2025, 2, 1, 0, 0);
        LocalDateTime timeForSubtask6 = LocalDateTime.of(2025, 3, 2, 1, 30);
        Subtask subtask5 = new Subtask("Test Subtask №3", "Belongs Epic №3",
                epic1.getId(), timeForSubtask5, 60);
        Subtask subtask6 = new Subtask("Test Subtask №3", "Belongs Epic №3",
                epic1.getId(), timeForSubtask6, 100);
        taskManager.createSubtask(subtask5);
        taskManager.createSubtask(subtask6);
        Subtask subtask7 = new Subtask("Test Subtask №3", "Belongs Epic №3",
                epic1.getId(), timeForSubtask5, 60);
        subtask7.setId(subtask5.getId());
        subtask7.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask7);
        Subtask subtask8 = new Subtask("Test Subtask №4", "Belongs Epic №4",
                epic1.getId(), timeForSubtask6, 100);
        subtask8.setId(subtask6.getId());
        subtask8.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask8);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subtask7.getStatus());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subtask8.getStatus());

        taskManager.cleanAllTasks();
        taskManager.cleanAllEpic();
    }

    @Test
    void testTaskTimeOverlap() {
        LocalDateTime timeForTask = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime timeForTask2 = LocalDateTime.of(2026, 1, 2, 0, 0);
        Task task1 = new Task("Task 1", "Description 1", timeForTask, 60);
        Task task2 = new Task("Task 2", "Description 2", timeForTask, 60);
        Task task3 = new Task("Task 2", "Description 2", timeForTask2, 60);
        assertTrue(task1.hasTimeOverlap(task2));
        assertFalse(task1.hasTimeOverlap(task3));
    }

    @Test
    void testCalculateEpicsTime() {
        Epic epic1 = new Epic("Test Epic №1", "№1");
        taskManager.createEpic(epic1);

        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 1, 1, 30);
        Subtask subtask1 = new Subtask("Test Subtask №1", "Belongs Epic №1",
                epic1.getId(), timeForSubtask1, 15);
        Subtask subtask2 = new Subtask("Test Subtask №2", "Belongs Epic №1",
                epic1.getId(), timeForSubtask2, 30);
        taskManager.createSubtask(subtask1);
        Assertions.assertEquals(epic1.getStartTime(), subtask1.getStartTime());
        Assertions.assertEquals(epic1.getEndTime(), subtask1.getEndTime());
        taskManager.createSubtask(subtask2);
        Assertions.assertEquals(epic1.getStartTime(), subtask1.getStartTime());
        Assertions.assertEquals(epic1.getEndTime(), subtask2.getEndTime());
        Assertions.assertEquals(45, epic1.getDuration().toMinutes());

        taskManager.removeSubtask(subtask1.getId());
        Assertions.assertEquals(epic1.getStartTime(), subtask2.getStartTime());
        Assertions.assertEquals(epic1.getEndTime(), subtask2.getEndTime());
        Assertions.assertEquals(30, epic1.getDuration().toMinutes());

        taskManager.removeSubtask(subtask2.getId());
        Assertions.assertNull(epic1.getStartTime());
        Assertions.assertNull(epic1.getEndTime());
        Assertions.assertNull(epic1.getDuration());
    }
}

