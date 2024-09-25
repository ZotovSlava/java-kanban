public class Subtask extends Task {

    int epicLinkId;

    protected Subtask(String name, String description, int epicLinkID) {
        super(name, description);
        this.epicLinkId = epicLinkID;
    }

    @Override
    public String toString() {
        return "Имя: " + name + "; " + "Статус: " + status + "; " + " Код подзадачи: " + id
                + "; " + "Код эпика: " + epicLinkId + " | ";
    }
}
