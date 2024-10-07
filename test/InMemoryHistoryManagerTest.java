import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    Task task1 = new Task("Задача №1", "№1");
    Task task2 = new Task("Задача №2", "№2");
    Task task3 = new Task("Задача №3", "№3");
    Task task4 = new Task("Задача №4", "№4");
    Task task5 = new Task("Задача №5", "№5");
    Task task6 = new Task("Задача №6", "№6");
    Task task7 = new Task("Задача №7", "№7");
    Task task8 = new Task("Задача №8", "№8");
    Task task9 = new Task("Задача №9", "№9");
    Task task10 = new Task("Задача №10", "№10");
    Task task11 = new Task("Задача №11", "№11");

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);
        inMemoryHistoryManager.add(task7);
        inMemoryHistoryManager.add(task8);
        inMemoryHistoryManager.add(task9);
        inMemoryHistoryManager.add(task10);
    }

    @Test
    void shouldAddTask() {
        assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void shouldOverwriteTask() {
        inMemoryHistoryManager.add(task11);

        assertEquals(task2, inMemoryHistoryManager.getHistory().get(0));
        assertEquals(task11, inMemoryHistoryManager.getHistory().get(9));
    }

    @Test
    void shouldSavePreviousVersionTask() {
        Task task2 = new Task("Задача №21", "№21");

        assertFalse(task2.equals(inMemoryHistoryManager.getHistory().get(1)));

        task2.name = "Задача №2";
        task2.description = "№2";

        assertTrue(task2.equals(inMemoryHistoryManager.getHistory().get(1)));
    }
}