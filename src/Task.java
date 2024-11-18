import java.util.Objects;

public class Task {

    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType type = TaskType.TASK;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return id + ", " + type + ", " + name + ", " + status + ", " + description;
    }
}
