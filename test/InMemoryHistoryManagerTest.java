import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    Managers managers = new Managers();
    TaskManager taskManager = managers.getDefaultManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    Task task1 = new Task("Задача №1", "№1");
    Task task2 = new Task("Задача №2", "№2");
    Task task3 = new Task("Задача №3", "№3");
    Task task4 = new Task("Задача №4", "№4");
    Task task5 = new Task("Задача №5", "№5");
    Epic epic1 = new Epic("Эпик №1", "№1");
    Epic epic2 = new Epic("Эпик №2", "№2");
    Task task6 = new Task("Задача №6", "№6");

    @AfterEach
    void afterEach() {
        inMemoryHistoryManager.clearHistory();
    }

    @Test
    void shouldAddTask() {
        assertEquals(0, inMemoryHistoryManager.getHistory().size());

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);

        assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void shouldRemoveDuplicateTaskFromTheHistory() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);

        assertEquals(3, inMemoryHistoryManager.getHistory().size());
    }


    @Test
    void shouldRemoveEpicAndHisSubtaskFromHistoryIfRemoveEpic() {
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 1, 30);
        taskManager.createTask(task4);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Cабтаск №1.1", "№1.1",
                epic1.getId(), timeForSubtask1, 500);
        Subtask subtask2 = new Subtask("Cабтаск №2.1", "№2.1",
                epic1.getId(), timeForSubtask2, 400);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
        assertEquals(4, inMemoryHistoryManager.getHistory().size());

        taskManager.removeEpic(epic1.getId());
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskFromHistoryIfRemoveTask() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        assertEquals(3, inMemoryHistoryManager.getHistory().size());

        taskManager.removeTask(task2.getId());
        assertEquals(2, inMemoryHistoryManager.getHistory().size());
    }


    @Test
    void HistoryShouldSaveCorrectOrderAfterAddAndDeletionTask() {
        LocalDateTime timeForSubtask1 = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime timeForSubtask2 = LocalDateTime.of(2025, 1, 2, 1, 30);
        LocalDateTime timeForSubtask3 = LocalDateTime.of(2025, 2, 2, 1, 30);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Cабтаск №1.1", "№1.1",
                epic1.getId(), timeForSubtask1, 40);
        Subtask subtask2 = new Subtask("Cабтаск №2.1", "№2.1",
                epic1.getId(), timeForSubtask2, 90);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Cабтаск №1.2", "№1.2",
                epic2.getId(), timeForSubtask3, 60);
        taskManager.createSubtask(subtask3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(epic2);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
        inMemoryHistoryManager.add(subtask3);

        ArrayList<Task> testTasks = new ArrayList<>();
        testTasks.add(task6);
        testTasks.add(task4);
        testTasks.add(task5);
        testTasks.add(subtask3);
        testTasks.add(epic2);
        testTasks.add(task3);
        testTasks.add(task2);

        taskManager.removeEpic(epic1.getId());
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task6);
        taskManager.removeTask(task1.getId());

        assertEquals(testTasks, inMemoryHistoryManager.getHistory());
        taskManager.cleanAllTasks();
        taskManager.cleanAllEpic();
    }
}
