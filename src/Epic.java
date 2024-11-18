import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    protected TaskType type = TaskType.EPIC;

    protected Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return id + ", " + type + ", " + name + ", " + status + ", " + description;
    }
}
