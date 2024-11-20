import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    protected Epic(String name, String description) {
        super(name, description);
        type = TaskType.EPIC;
    }
}
