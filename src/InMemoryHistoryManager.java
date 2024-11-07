import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final static MyLinkedList<Task> historyList = new MyLinkedList<>();
    private final static HashMap<Integer, Node<Task>> taskMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (taskMap.isEmpty()) {
                taskMap.put(task.getId(), historyList.linkLast(task));

            } else if (taskMap.containsKey(task.getId())) {
                if (taskMap.size() == 1) {
                    return;
                }
                removeNode(taskMap.get(task.getId()));
                taskMap.put(task.getId(), historyList.linkLast(task));

            } else {
                int id = historyList.last.element.getId();
                Node<Task> n = historyList.linkLast(task);
                taskMap.put(task.getId(), n);
                taskMap.get(id).next = n;
            }
        }
    }


    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            removeNode(taskMap.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    public void clearHistory() {
        taskMap.clear();
        historyList.first = null;
        historyList.last = null;
    }

    private void removeNode(Node<Task> node) {
        if (node.prev == null) {
            if (taskMap.size() == 1) {
                taskMap.remove(node.element.getId());
                historyList.first = null;
                historyList.last = null;
                return;
            }
            node.next.prev = null;
            taskMap.put(node.next.element.getId(), node.next);
            historyList.first = node.next;
            taskMap.remove(node.element.getId());

        } else if (node.next == null) {
            node.prev.next = null;
            taskMap.put(node.prev.element.getId(), node.prev);
            historyList.last = node.prev;
            taskMap.remove(node.element.getId());

        } else {
            node.prev.next = node.next;
            taskMap.put(node.prev.element.getId(), node.prev);
            node.next.prev = node.prev;
            taskMap.put(node.next.element.getId(), node.next);
            taskMap.remove(node.element.getId());
        }
    }


    private static class MyLinkedList<Task> {
        Node<Task> first;
        Node<Task> last;

        private Node<Task> linkLast(Task task) {
            final Node<Task> l = last;
            final Node<Task> newNode = new Node<>(l, task, null);
            last = newNode;
            if (l == null)
                first = newNode;
            else
                l.next = newNode;

            return last;
        }

        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            if (last == null) {
                return tasks;
            }

            Node<Task> n = last;
            tasks.add(n.element);

            while (n.prev != null) {
                tasks.add(n.prev.element);
                n = n.prev;
            }
            return tasks;
        }
    }
}


