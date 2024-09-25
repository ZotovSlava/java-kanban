import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private int nextId = 1;

    public void getAllTasks() {
        System.out.println(taskHashMap.values());
    }

    public void getAllEpics() {
        System.out.println(epicHashMap.values());
    }

    public void getAllSubtasks() {
        System.out.println(subtaskHashMap.values());
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
        task.id = nextId++;
        taskHashMap.put(task.id, task);
    }

    public void createEpic(Epic epic) {
        epic.id = nextId++;
        epicHashMap.put(epic.id, epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.id = nextId++;
        subtaskHashMap.put(subtask.id, subtask);

        Epic epic = epicHashMap.get(subtask.epicLinkId);
        epic.subtaskIds.add(subtask.id);

        epicStatusManager(epic.id);
    }


    public void updateTask(Task task) {
        taskHashMap.put(task.id, task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskHashMap.put(subtask.id, subtask);
        epicStatusManager(subtask.epicLinkId);
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

        Epic epic = epicHashMap.get(subtask.epicLinkId);
        epic.subtaskIds.remove((Integer) subtaskId);

        subtaskHashMap.remove(subtaskId);

        epicStatusManager(epic.id);
    }

    public void epicStatusManager(int epicId) {

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

    public void changeTaskStatus(TaskStatus status, Task task) {
        task.setStatus(status);
    }

    public void changeSubtaskStatus(TaskStatus status, Subtask subtask) {
        subtask.setStatus(status);
        epicStatusManager(subtask.epicLinkId);
    }
}
