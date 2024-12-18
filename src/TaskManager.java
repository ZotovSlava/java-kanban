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

    boolean createTask(Task task);

    void createEpic(Epic epic);

    boolean createSubtask(Subtask subtask);

    boolean updateTask(Task task);

    void updateEpic(Epic epic);

    boolean updateSubtask(Subtask subtask);

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    List<Subtask> getEpicSubtasks(int epicId);

    SortedSet<Task> getPrioritizedTasks();

    List<Task> getHistory();
}
