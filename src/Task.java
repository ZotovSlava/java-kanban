public class Task {

    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
    }

    protected TaskStatus getStatus() {
        return status;
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Имя: " + name + "; " + "Статус: " + status + "; " + "Код задачи: " + id + " | ";
    }
}
