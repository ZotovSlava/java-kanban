import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class Epic extends Task {
    protected LocalDateTime endTime;
    ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
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

    public void calculateEpicTime(Map<Integer, Subtask> subtasksMap) {

        if (subtaskIds.size() == 1) {
            Subtask subtask = subtasksMap.get(subtaskIds.getFirst());
            setStartTime(subtask.getStartTime());
            setDuration(subtask.getDuration());
            setEndTime(subtask.getEndTime());
        } else {
            setDuration(Duration.ZERO);

            for (Integer subtaskId : subtaskIds) {
                Subtask subtask = subtasksMap.get(subtaskId);

                if (getStartTime().isAfter(subtask.getStartTime())) {
                    setStartTime(subtask.getStartTime());
                } else if (getEndTime().isBefore(subtask.getEndTime())) {
                    setEndTime(subtask.getEndTime());
                }
                setDuration(getDuration().plus(subtask.getDuration()));
            }
        }
    }
}
