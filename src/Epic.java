import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected LocalDateTime endTime;
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    protected Epic(String name, String description) {
        super(name, description);
        type = TaskType.EPIC;
    }

    @Override
    protected LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
