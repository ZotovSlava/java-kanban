public class Node<T> {
    public T element;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T element, Node<T> next) {
        this.element = element;
        this.next = next;
        this.prev = prev;
    }
}
