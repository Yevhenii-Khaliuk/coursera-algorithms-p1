import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item has to be not null");
        }

        Node<Item> newNode = new Node<>();
        newNode.item = item;
        if (first != null) {
            newNode.next = first;
            first.previous = newNode;
        }
        first = newNode;
        if (last == null) {
            last = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item has to be not null");
        }

        Node<Item> newNode = new Node<>();
        newNode.item = item;
        if (last != null) {
            newNode.previous = last;
            last.next = newNode;
        }
        last = newNode;
        if (first == null) {
            first = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        Item item = first.item;
        if (first.next != null) {
            first.next.previous = null;
        }
        first = first.next;
        if (first == null) {
            last = null;
        }
        size--;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        Item item = last.item;
        if (last.previous != null) {
            last.previous.next = null;
        }
        last = last.previous;
        if (last == null) {
            first = null;
        }
        size--;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeFrontToBackIterator();
    }

    private class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> previous;
    }

    private class DequeFrontToBackIterator implements Iterator<Item> {

        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No more items to return");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove() operation is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        System.out.println("Newly created deque size: " + deque.size());
        System.out.println("Deque is empty: " + deque.isEmpty());
        deque.addFirst("one");
        System.out.println("Size after adding first element: " + deque.size());
        System.out.println("Deque is empty: " + deque.isEmpty());
        String item = deque.removeLast();
        System.out.println("Removed last item: " + item);
        System.out.println("Size after removing last: " + deque.size());
        deque.addLast("two");
        System.out.println("Size after adding last element: " + deque.size());
        item = deque.removeFirst();
        System.out.println("Removed first item: " + item);
        System.out.println("Size after removing first: " + deque.size());
        System.out.println("Deque is empty: " + deque.isEmpty());
        deque.addFirst("one");
        deque.addFirst("two");
        deque.addFirst("three");
        System.out.println("Using iterator:");
        for (String s : deque) {
            System.out.println(s);
        }
    }

}