import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File tempFile;

    private FileBackedTaskManager manager;

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("tasks", ".txt");
        manager = new FileBackedTaskManager(tempFile);
        manager.cleanAllTasks();
        manager.cleanAllEpic();
    }

    @AfterEach
    void afterEach() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() {

        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void shouldSaveAndLoadTasks() {

        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        manager.createTask(task1);
        manager.createTask(task2);

        assertEquals(2, manager.getAllTasks().size());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getAllTasks().size());
        assertTrue(loadedManager.getAllTasks().contains(task1));
        assertTrue(loadedManager.getAllTasks().contains(task2));

    }

    @Test
    void shouldSaveAndLoadEpicsAndSubtasks() {

        Epic epic1 = new Epic("Epic1", "Description1");
        manager.createEpic(epic1);
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSub1",
                epic1.getId(), timeForSubtask1, 70);
        manager.createSubtask(subtask1);

        assertEquals(1, manager.getAllEpics().size());
        assertEquals(1, manager.getAllSubtasks().size());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
        assertTrue(loadedManager.getAllEpics().contains(epic1));
        assertTrue(loadedManager.getAllSubtasks().contains(subtask1));
        assertEquals(2, loadedManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldSaveAndLoadAllChanging() {

        Task task1 = new Task("Task1", "Description1");
        manager.createTask(task1);

        Epic epic1 = new Epic("Epic1", "Description1");
        manager.createEpic(epic1);
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSub1", epic1.getId(),
                timeForSubtask1, 60);
        manager.createSubtask(subtask1);

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(1, manager.getAllSubtasks().size());


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
        assertTrue(loadedManager.getAllTasks().contains(task1));
        assertTrue(loadedManager.getAllEpics().contains(epic1));
        assertTrue(loadedManager.getAllSubtasks().contains(subtask1));

        manager.removeTask(task1.getId());

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());

        manager.cleanAllEpic();

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testSaveDoesNotThrowException() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 30);

        Assertions.assertDoesNotThrow(() -> {
            manager.createTask(task);
        });
    }

    @Test
    void testLoadFromFileDoesNotThrowException() {
        Assertions.assertDoesNotThrow(() -> {
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        });
    }

    @Test
    void testSaveThrowsExceptionForInvalidFile() {
        File invalidFile = new File("/invalid_path/tasks.txt");
        FileBackedTaskManager invalidManager = new FileBackedTaskManager(invalidFile);

        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 30);

        Assertions.assertThrows(ManagerSaveException.class, () -> {
            invalidManager.createTask(task);
        });
    }

    @Test
    void testLoadThrowsExceptionForInvalidFile() {
        File invalidFile = new File("/invalid_path/tasks.txt");

        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(invalidFile);
        });
    }
}
