import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private int nextId = 1;

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    public void cleanAllTasks() {
        taskHashMap.clear();
    }

    public void cleanAllEpic() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public void cleanAllSubtask() {
        subtaskHashMap.clear();

        for (Epic epic : epicHashMap.values()) {
            epic.subtaskIds.clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public Task getTask(int taskId) {
        return taskHashMap.get(taskId);
    }

    public Epic getEpic(int epicId) {
        return epicHashMap.get(epicId);
    }

    public Subtask getSubtask(int subtaskId) {
        return subtaskHashMap.get(subtaskId);
    }


    public void createTask(Task task) {
        task.setId(nextId++);
        taskHashMap.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epicHashMap.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtaskHashMap.put(subtask.getId(), subtask);

        Epic epic = epicHashMap.get(subtask.getEpicLinkId());
        epic.subtaskIds.add(subtask.getId());

        calculateEpicStatus(epic.getId());
    }


    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
        calculateEpicStatus(subtask.getEpicLinkId());
    }

    public void removeTask(int taskId) {
        taskHashMap.remove(taskId);
    }

    public void removeEpic(int epicId) {
        Epic epic = epicHashMap.get(epicId);

        if (epic == null) {
            System.out.println("Такого эпика нет.");
            return;
        }

        for (Integer subtaskId : epic.subtaskIds) {
            subtaskHashMap.remove(subtaskId);
        }

        epicHashMap.remove(epicId);
    }

    public void removeSubtask(int subtaskId) {
        Subtask subtask = subtaskHashMap.get(subtaskId);

        if (subtask == null) {
            System.out.println("Такой подзадачи нет.");
            return;
        }

        Epic epic = epicHashMap.get(subtask.getEpicLinkId());
        epic.subtaskIds.remove((Integer) subtaskId);

        subtaskHashMap.remove(subtaskId);

        calculateEpicStatus(epic.getId());
    }

    public void getEpicSubtasks(int epicId) {
        Epic epic = epicHashMap.get(epicId);

        if (epic.subtaskIds.isEmpty()) {
            System.out.println("Подзадач нет!");
        }

        for (Integer subtaskId : epic.subtaskIds) {
            Subtask subtask = subtaskHashMap.get(subtaskId);
            System.out.println(subtask.toString());
        }
    }

    private void calculateEpicStatus(int epicId) {

        int doneSubtask = 0;
        int newSubtask = 0;
        int inProgressSubtask = 0;

        Epic epic = epicHashMap.get(epicId);

        for (Integer subtaskID : epic.subtaskIds) {
            Subtask subtask = subtaskHashMap.get(subtaskID);

            if (subtask.getStatus() == TaskStatus.NEW) {
                newSubtask++;
            }
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                inProgressSubtask++;
            }
            if (subtask.getStatus() == TaskStatus.DONE) {
                doneSubtask++;
            }
        }

        if (doneSubtask == 0 && inProgressSubtask == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else if (newSubtask == 0 && inProgressSubtask == 0) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
