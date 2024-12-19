import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private static Node<Task> first;
    private static Node<Task> last;
    private static final HashMap<Integer, Node<Task>> taskMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (taskMap.isEmpty()) {
                taskMap.put(task.getId(), linkLast(task));

            } else if (taskMap.containsKey(task.getId())) {
                if (taskMap.size() == 1) {
                    return;
                }
                removeNode(taskMap.get(task.getId()));
                taskMap.put(task.getId(), linkLast(task));

            } else {
                int id = last.element.getId();
                Node<Task> n = linkLast(task);
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
        return getTasks();
    }

    public void clearHistory() {
        taskMap.clear();
        first = null;
        last = null;
    }

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

    private void removeNode(Node<Task> node) {
        if (node.prev == null) {
            if (taskMap.size() == 1) {
                taskMap.remove(node.element.getId());
                first = null;
                last = null;
                return;
            }
            node.next.prev = null;
            taskMap.put(node.next.element.getId(), node.next);
            first = node.next;
            taskMap.remove(node.element.getId());

        } else if (node.next == null) {
            node.prev.next = null;
            taskMap.put(node.prev.element.getId(), node.prev);
            last = node.prev;
            taskMap.remove(node.element.getId());

        } else {
            node.prev.next = node.next;
            taskMap.put(node.prev.element.getId(), node.prev);
            node.next.prev = node.prev;
            taskMap.put(node.next.element.getId(), node.next);
            taskMap.remove(node.element.getId());
        }
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

    private static class Node<T> {
        public T element;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T element, Node<T> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}


