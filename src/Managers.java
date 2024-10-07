public class Managers {
    public TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }

    public InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
