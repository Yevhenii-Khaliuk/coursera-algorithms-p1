import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 8;

    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item has to be not null");
        }

        if (size == items.length) {
            resize(items.length * 2);
        }
        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int randomIndex = StdRandom.uniform(size);
        Item item = items[randomIndex];
        items[randomIndex] = items[--size];
        items[size] = null;
        if (items.length > DEFAULT_CAPACITY && size == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int randomIndex = StdRandom.uniform(size);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int newCapacity) {
        Item[] newItems = (Item[]) new Object[newCapacity];
        System.arraycopy(items, 0, newItems, 0, size);
        items = newItems;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] shuffledItems;
        private int current;

        public RandomizedQueueIterator() {
            shuffledItems = (Item[]) new Object[size];
            System.arraycopy(items, 0, shuffledItems, 0, shuffledItems.length);
            StdRandom.shuffle(shuffledItems);
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < shuffledItems.length;
        }

        @Override
        public Item next() {
            if (current == shuffledItems.length) {
                throw new NoSuchElementException("Queue is empty");
            }

            return shuffledItems[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove() operation is not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        System.out.println("Size of empty queue: " + queue.size());
        System.out.println("Queue is empty: " + queue.isEmpty());
        queue.enqueue("one");
        System.out.println("Queue size after first enqueue: " + queue.size());
        System.out.println("Queue is empty: " + queue.isEmpty());
        queue.enqueue("two");
        queue.enqueue("three");
        queue.enqueue("four");
        System.out.println("Queue size after 4th enqueue: " + queue.size());
        System.out.println("Random sample: " + queue.sample());
        String item = queue.dequeue();
        System.out.println("Random item: " + item);
        System.out.println("Queue size after dequeue: " + queue.size());
        queue.enqueue("five");
        for (String s : queue) {
            System.out.println(s);
        }
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        System.out.println("Queue size after all items been dequeued: " + queue.size());
        System.out.println("Queue is empty: " + queue.isEmpty());
    }

}