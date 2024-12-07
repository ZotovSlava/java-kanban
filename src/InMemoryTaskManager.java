import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = new InMemoryHistoryManager();

    private static final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private static final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private static final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private static final SortedSet<Task> tasksSortedByTime = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );
    private int nextId = 1;

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    @Override
    public void cleanAllTasks() {
        taskHashMap.values().stream()
                .filter(task -> task.getStartTime() != null && task.getDuration() != null)
                .forEach(tasksSortedByTime::remove);
        taskHashMap.clear();
    }

    @Override
    public void cleanAllEpic() {
        epicHashMap.values().stream()
                .filter(epic -> epic.getStartTime() != null && epic.getDuration() != null)
                .forEach(tasksSortedByTime::remove);
        subtaskHashMap.values().forEach(tasksSortedByTime::remove);
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    @Override
    public void cleanAllSubtask() {
        subtaskHashMap.values().forEach(tasksSortedByTime::remove);
        subtaskHashMap.clear();
        epicHashMap.values().forEach(epic -> {
            tasksSortedByTime.remove(epic);
            epic.subtaskIds.clear();
            epic.setStatus(TaskStatus.NEW);
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        });
    }

    @Override
    public Task getTask(int taskId) {
        historyManager.add(taskHashMap.get(taskId));
        return taskHashMap.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {
        historyManager.add(epicHashMap.get(epicId));
        return epicHashMap.get(epicId);
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        historyManager.add(subtaskHashMap.get(subtaskId));
        return subtaskHashMap.get(subtaskId);
    }


    @Override
    public void createTask(Task task) {
        if ((task.getStartTime() == null && task.getDuration() == null) || add(task)) {
            task.setId(nextId++);
            taskHashMap.put(task.getId(), task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (add(subtask)) {
            Epic epic = epicHashMap.get(subtask.getEpicLinkId());

            if (!epic.subtaskIds.isEmpty()) {
                tasksSortedByTime.remove(epic);
            }
            subtask.setId(nextId++);
            subtaskHashMap.put(subtask.getId(), subtask);
            epic.subtaskIds.add(subtask.getId());
            calculateEpicStatus(epic.getId());
            epic.calculateEpicsTime(subtaskHashMap);
            tasksSortedByTime.add(epic);
        }
    }


    @Override
    public void updateTask(Task task) {
        if (getPrioritizedTasks().contains(taskHashMap.get(task.getId()))) {
            tasksSortedByTime.remove(taskHashMap.get(task.getId()));
        }

        if ((task.getStartTime() == null && task.getDuration() == null) || add(task)) {
            taskHashMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic.getStartTime() != null && epic.getDuration() != null) {
            tasksSortedByTime.remove(epicHashMap.get(epic.getId()));
            tasksSortedByTime.add(epic);
        }
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        tasksSortedByTime.remove(subtaskHashMap.get(subtask.getId()));

        if (add(subtask)) {
            subtaskHashMap.put(subtask.getId(), subtask);
            tasksSortedByTime.remove(epicHashMap.get(subtask.getEpicLinkId()));
            calculateEpicStatus(subtask.getEpicLinkId());
            Epic epic = epicHashMap.get(subtask.getEpicLinkId());
            epic.calculateEpicsTime(subtaskHashMap);
            tasksSortedByTime.add(epicHashMap.get(subtask.getEpicLinkId()));
        } else {
            tasksSortedByTime.add(subtaskHashMap.get(subtask.getId()));
        }
    }

    @Override
    public void removeTask(int taskId) {
        if (getPrioritizedTasks().contains(taskHashMap.get(taskId))) {
            tasksSortedByTime.remove(taskHashMap.get(taskId));
        }
        historyManager.remove(taskId);
        taskHashMap.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        Epic epic = epicHashMap.get(epicId);

        if (epicHashMap.get(epicId).getStartTime() != null && epicHashMap.get(epicId).getDuration() != null) {
            tasksSortedByTime.remove(epicHashMap.get(epicId));

            epic.subtaskIds.forEach(subtaskId -> {
                tasksSortedByTime.remove(subtaskHashMap.get(subtaskId));
                subtaskHashMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
        }
        epicHashMap.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        Epic epic = epicHashMap.get(subtaskHashMap.get(subtaskId).getEpicLinkId());
        epic.subtaskIds.remove((Integer) subtaskId);

        tasksSortedByTime.remove(epic);
        tasksSortedByTime.remove(subtaskHashMap.get(subtaskId));
        calculateEpicStatus(epic.getId());

        if (epic.subtaskIds.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        } else {
            epic.calculateEpicsTime(subtaskHashMap);
            tasksSortedByTime.add(epic);
        }
        historyManager.remove(subtaskId);
        subtaskHashMap.remove(subtaskId);
    }

    @Override
    public void getEpicSubtasks(int epicId) {
        Epic epic = epicHashMap.get(epicId);

        if (epic == null || epic.subtaskIds.isEmpty()) {
            System.out.println("Подзадач нет!");
            return;
        }

        epic.subtaskIds.stream()
                .map(subtaskHashMap::get)
                .forEach(System.out::println);

    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public static void setTaskHashMap(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public static void setEpicHashMap(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public static HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public static void setSubtaskHashMap(Subtask subtask) {
        subtaskHashMap.put(subtask.getId(), subtask);
    }

    public SortedSet<Task> getPrioritizedTasks() {
        return tasksSortedByTime;
    }

    public void setTasksSortedByTime(Task task) {
        tasksSortedByTime.add(task);
    }

    private boolean add(Task task) {
        if (tasksSortedByTime.stream()
                .filter(taskFromSet -> taskFromSet.getType() == TaskType.TASK || taskFromSet.getType() == TaskType.SUBTASK)
                .anyMatch(taskFromSet -> taskFromSet.hasTimeOverlap(task))) {
            System.out.println("Ошибка: новая задача пересекается с существующей задачей и не будет добавлена.");
            return false;
        } else {
            tasksSortedByTime.add(task);
            return true;
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
