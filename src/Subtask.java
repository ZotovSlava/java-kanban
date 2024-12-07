import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicLinkId;

    protected Subtask(String name, String description, int epicLinkID, LocalDateTime startTime, long taskTimeLimit) {
        super(name, description, startTime, taskTimeLimit);
        this.epicLinkId = epicLinkID;
        type = TaskType.SUBTASK;
    }

    protected int getEpicLinkId() {
        return epicLinkId;
    }

    @Override
    public String toString() {
        return id + ", " + type + ", " + name + ", " + status + ", " + description + ", " + epicLinkId +
                ", " + startTime + ", " + duration.toMinutes();
    }
}
