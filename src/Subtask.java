public class Subtask extends Task {

    private final int epicLinkId;
    protected TaskType type = TaskType.SUBTASK;

    protected Subtask(String name, String description, int epicLinkID) {
        super(name, description);
        this.epicLinkId = epicLinkID;
    }

    protected int getEpicLinkId() {
        return epicLinkId;
    }

    @Override
    public String toString() {
        return id + ", "+ type+ ", " + name + ", " + status + ", " + description + ", " + epicLinkId;
    }
}
