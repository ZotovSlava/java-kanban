public class Subtask extends Task {

    private final int epicLinkId;

    protected Subtask(String name, String description, int epicLinkID) {
        super(name, description);
        this.epicLinkId = epicLinkID;
    }

    protected int getEpicLinkId() {
        return epicLinkId;
    }

    @Override
    public String toString() {
        return "Имя: " + name + "; " + "Статус: " + status + "; " + " Код подзадачи: " + id
                + "; " + "Код эпика: " + epicLinkId + " | ";
    }


}
