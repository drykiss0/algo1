import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
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
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (first == null) {
            last = node;
        } else {
            first.previous = node;
            node.next = first;
        }
        first = node;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (last == null) {
            first = node;
        } else {
            last.next = node;
            node.previous = last;
        }
        last = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.previous = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.previous;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                Item item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    // unit testing (optional)
    public static void main(String[] args) {

        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        StdOut.println("-- Iterating --");
        for (Integer el: deque) {
            StdOut.print(el + " ");
        }
        StdOut.println();
        StdOut.println("-- Removing --");
        int size = deque.size();
        for (int i = 0; i < size; i++) {
            StdOut.print(deque.removeLast());
        }
        StdOut.println();
        StdOut.println("-- Iterating again --");
        for (Integer el: deque) {
            StdOut.print(el + " ");
        }
        StdOut.println();
        StdOut.println("-- TESTS --");

        deque = new Deque<>();
        deque.addFirst(1);
        deque.removeFirst();
        deque.addLast(3);
        deque.addFirst(4);
        deque.addLast(5);
        StdOut.println(deque.removeLast());

        StdOut.println("-- FAILED iterator --");
        for (Integer el: deque) {
            StdOut.print(el + " ");
        }
    }
}