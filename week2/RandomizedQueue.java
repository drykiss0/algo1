import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int afterLast;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return afterLast == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return afterLast;
    }

    private void resize(int newSize) {
       Item[] newItems = (Item[]) new Object[newSize];
       for (int i = 0; i < afterLast; i++) {
           newItems[i] = items[i];
       }
       items = newItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (afterLast == items.length) {
            resize(items.length * 2);
        }
        items[afterLast++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int position = StdRandom.uniform(afterLast);
        Item item = items[position];
        items[position] = items[afterLast - 1];
        items[afterLast - 1] = null;
        afterLast--;
        if (afterLast > 0 && items.length / afterLast == 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(afterLast)];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int[] randomIndices;
        private int afterLastIndex;

        public RandomizedQueueIterator() {
            randomIndices = new int[size()];
            for (int i = 0; i < randomIndices.length; i++) {
                randomIndices[i] = i;
            }
            afterLastIndex = randomIndices.length;
        }

        @Override
        public boolean hasNext() {
            return afterLastIndex > 0;
        }

        @Override
        public Item next() {
            if (afterLastIndex < 1) {
                throw new NoSuchElementException();
            }
            int randomIndex = StdRandom.uniform(afterLastIndex);
            Item item = items[randomIndices[randomIndex]];
            randomIndices[randomIndex] = randomIndices[afterLastIndex - 1];
            afterLastIndex--;
            return item;
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (optional)
    public static void main(String[] args) {

        RandomizedQueue<Integer> rQueue = new RandomizedQueue<>();
        StdOut.println("-- Enqueue --");
        for (Integer i = 1; i < 10; i++) {
            rQueue.enqueue(i);
            StdOut.print(i + " ");
        }

        StdOut.println();
        StdOut.println("-- Iterating --");
        for (Integer el: rQueue) {
            StdOut.print(el + " ");
        }
        StdOut.println();
        StdOut.println("-- Dequeue --");
        int size = rQueue.size();
        for (int i = 0; i < size; i++) {
            StdOut.print(rQueue.dequeue() + " ");
        }
        StdOut.println();
        StdOut.println("-- Iterating again --");
        for (Integer el: rQueue) {
            StdOut.print(el + " ");
        }
    }
}