import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File autoSaveFile;

    public FileBackedTaskManager(File autoSaveFile) {
        this.autoSaveFile = autoSaveFile;
    }

    @Override
    public void cleanAllTasks() {
        super.cleanAllTasks();
        save();
    }

    @Override
    public void cleanAllEpic() {
        super.cleanAllEpic();
        save();
    }

    @Override
    public void cleanAllSubtask() {
        super.cleanAllSubtask();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String supLine = bufferedReader.readLine();
                Task task = fromString(supLine);
                if (task != null) {
                    switch (task.getType()) {
                        case TASK -> manager.setTaskHashMap(task);
                        case EPIC -> manager.setEpicHashMap((Epic) task);
                        case SUBTASK -> manager.setSubtaskHashMap((Subtask) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при восстановлении данных из файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] splitLine = value.split(", ");

        TaskType taskType = TaskType.valueOf(splitLine[1]);

        switch (taskType) {
            case TASK -> {
                Task task = new Task(splitLine[2], splitLine[4]);
                task.setId(Integer.parseInt(splitLine[0]));
                task.setStatus(TaskStatus.valueOf(splitLine[3]));
                task.setType(TaskType.TASK);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(splitLine[2], splitLine[4]);
                epic.setId(Integer.parseInt(splitLine[0]));
                epic.setStatus(TaskStatus.valueOf(splitLine[3]));
                epic.setType(TaskType.EPIC);
                return epic;
            }
            case SUBTASK -> {
                Subtask subtask = new Subtask(splitLine[2], splitLine[4], Integer.parseInt(splitLine[5]));
                subtask.setId(Integer.parseInt(splitLine[0]));
                subtask.setStatus(TaskStatus.valueOf(splitLine[3]));
                subtask.setType(TaskType.SUBTASK);
                Epic epic = getEpicHashMap().get(subtask.getEpicLinkId());
                epic.subtaskIds.add(subtask.getId());
                return subtask;
            }
            default -> {
                return null;
            }
        }
    }

    private void save() {
        List<Task> list = new ArrayList<>();
        list.addAll(getAllTasks());
        list.addAll(getAllEpics());
        list.addAll(getAllSubtasks());

        try (Writer fileWriter = new FileWriter(autoSaveFile, false)) {
            for (Task task : list) {
                fileWriter.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", e);
        }
    }
}
