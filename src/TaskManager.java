import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void cleanAllTasks();

    void cleanAllEpic();

    void cleanAllSubtask();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    Subtask getSubtask(int subtaskId);

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    void getEpicSubtasks(int epicId);

    SortedSet<Task> getPrioritizedTasks();

    List<Task> getHistory();
}
