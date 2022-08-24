import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int count;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst; // form an arrow from new first to old first
        if (oldFirst == null) // meaning old first is null
            last = first;
        else
            oldFirst.previous = first;
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (oldLast == null) // meaning old last is null
            first = last;
        else
            oldLast.next = last;
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        Node oldFirst = first;
        Item item = oldFirst.item;
        first = first.next;
        oldFirst.next = null;
        if (first != null) // meaning there are at least two nodes in the original link
            first.previous = null;
        else
            last = null;
        count--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        Node oldLast = last;
        Item item = oldLast.item;
        last = last.previous;
        oldLast.previous = null;
        if (last != null) // meaning there are at least two nodes in the original link
            last.next = null;
        else
            first = null;
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("doesn't support remove operation");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Deque has no more elements");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<String> a = new Deque<String>();
        a.addFirst("one");
        a.addFirst("previous one");
        a.addLast("two");
        StdOut.println("start removing");
        StdOut.println(a.removeLast());
        StdOut.println(a.removeLast());
        StdOut.println(a.removeFirst());
        for (String s : a)
            StdOut.println(s);
        StdOut.println(a.isEmpty());
        StdOut.println(a.size());


    }

}
