import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node head;
    private int count;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        head = null;
        count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item is null");

        // create the new last node
        Node newLast = new Node();
        newLast.item = item;

        // if the list is empty, make the new node as head
        if (head == null) {
            head = newLast;
            count++;
            return;
        }

        // find the last node
        Node last = head;
        while (last.next != null)
            last = last.next;

        // make the pointer of new last and the last node pointing each other
        last.next = newLast;
        newLast.previous = last;
        count++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (count == 0)
            throw new NoSuchElementException("Randomized Queue has no more elements");
        Node temp = head;
        int rand = (int) (Math.random() * count); // generate rand [0, count-1]
        for (int i = 0; i < rand; i++) {
            temp = temp.next;
        }
        Item item = temp.item;

        // if temp points to the head
        if (rand == 0) {
            head = head.next;
            if (temp.next != null)
                temp.next.previous = null;
            temp.next = null;
        }
        // if temp points to the tail
        else if (rand == (count - 1)) {
            temp.previous.next = null;
            temp.previous = null;
        }
        // other situations (temp points to the middle)
        else {
            temp.previous.next = temp.next;
            temp.next.previous = temp.previous;

        }
        count--;
        return item;

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (count == 0)
            throw new NoSuchElementException("Randomized Queue has no more elements");
        Node temp = head;
        int rand = (int) (Math.random() * count); // generate rand [0, count-1]
        for (int i = 0; i < rand; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        private int tempCount = count;
        int[] randItem = random(tempCount);

        public boolean hasNext() {
            return (tempCount == 0);
        }

        public void remove() {
            throw new UnsupportedOperationException("doesn't support remove operation");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Randomized Queue has no more elements");
            Node current = head;
            for (int i = 0; i < randItem[tempCount - 1]; i++) {
                current = current.next;
            }
            tempCount--;
            return current.item;
        }

        // generate an Item[] to store items generated at random.
        public int[] random(int count) {
            int[] temp = new int[count];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = i + 1;
            }
            int[] randomedArr = new int[count];
            for (int i = 0; i < randomedArr.length; i++) {
                int index = (int) (Math.random() * (temp.length - i));
                randomedArr[i] = temp[index];
                temp[index] = temp[temp.length - 1 - i];
            }
            for (int i = 0; i < randomedArr.length; i++) {
                StdOut.println(randomedArr[i]);
            }
            return randomedArr;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        /* RandomizedQueue<Integer> a = new RandomizedQueue<>();
        a.enqueue(1);
        a.enqueue(2);
        a.enqueue(3);
        a.enqueue(4);
        StdOut.println("whether list is empty: " + a.isEmpty());
        StdOut.println("size is: " + a.size());
        StdOut.println("take a sample: " + a.sample());
        StdOut.println("testing dequeue: " + a.dequeue());
        StdOut.println("testing iterator: ");
        for (String s : a)
            StdOut.println(s);
         */
        RandomizedQueue<Integer> a = new RandomizedQueue<>();
        a.enqueue(2);
        a.enqueue(44);
        a.enqueue(10);
        a.enqueue(15);
        for (Integer s : a)
            StdOut.println(s);
        StdOut.println("size of the old queue is: " + a.size());

        a.enqueue(32);
        StdOut.println("size of the new queue is: " + a.size());
        for (Integer s : a)
            StdOut.println(s);



        /*
        // test multiple iterators
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int b : queue) {
            for (int c : queue)
                StdOut.print(b + "-" + c + " ");
            StdOut.println();
        }

         */

    }

}
