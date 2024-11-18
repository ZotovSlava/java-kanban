import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("tasks", ".txt");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void afterEach() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() {

        manager.cleanAllTasks();
        manager.cleanAllEpic();
        manager.cleanAllSubtask();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());

    }

    @Test
    void shouldSaveAndLoadTasks() {

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.createTask(task1);
        manager.createTask(task2);


        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);

        assertEquals(2, loadedManager.getAllTasks().size());
        assertTrue(loadedManager.getAllTasks().contains(loadedManager.getTask(task1.getId())));
        assertTrue(loadedManager.getAllTasks().contains(loadedManager.getTask(task2.getId())));

    }

    @Test
    void shouldSaveAndLoadAllTaskTypes() {

        Task task = new Task("Task", "Description");
        manager.createTask(task);

        Epic epic = new Epic("Epic", "EpicDescription");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "SubDescription", epic.getId());
        manager.createSubtask(subtask);


        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());

        assertTrue(loadedManager.getAllTasks().contains(loadedManager.getTask(task.getId())));
        assertTrue(loadedManager.getAllEpics().contains(loadedManager.getEpic(epic.getId())));
        assertTrue(loadedManager.getAllSubtasks().contains(loadedManager.getSubtask(subtask.getId())));

    }
}
