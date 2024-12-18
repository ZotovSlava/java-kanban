import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        type = TaskType.TASK;
    }

    public Task(String name, String description, LocalDateTime startTime, long taskTimeLimit) {
        this.name = name;
        this.description = description;
        status = TaskStatus.NEW;
        type = TaskType.TASK;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(taskTimeLimit);
    }

    protected TaskStatus getStatus() {
        return status;
    }
    protected String getName() {
        return name;
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

    protected TaskType getType() {
        return type;
    }

    protected void setType(TaskType type) {
        this.type = type;
    }

    protected LocalDateTime getStartTime() {
        return startTime;
    }

    protected void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    protected Duration getDuration() {
        return duration;
    }

    protected void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean hasTimeOverlap(Task task) {
        return !(this.getEndTime().isBefore(task.getStartTime()) || this.getStartTime().isAfter(task.getEndTime()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        if (startTime != null) {
            return id + ", " + type + ", " + name + ", " + status + ", " + description + ", " + startTime +
                    ", " + duration.toMinutes();
        } else {
            return id + ", " + type + ", " + name + ", " + status + ", " + description;
        }
    }
}
