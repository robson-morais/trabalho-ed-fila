import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayQueue<Item> implements Iterable<Item> {

    // initial capacity of underlaying resizing array
    private static final int INIT_CAPACITY = 8;
    private Item[] q;  // queue elements
    private int n;     // number of elements on queue
    private int first; // index of first element of queue
    private int last;  // index of next avaiable slot

    /*
     * Initializes an empty queue.
     */
    public ResizingArrayQueue() {
        q = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
        first = 0;
        last = 0;
    }

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */

     public int size() {
        return n;
     }

     // resize the underlying array
     private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last = n;
     }

     /**
      * Adds the item to this queue
      * @param item the item to add
      */
      public void enqueue(Item item) {
        // double size of array if necessary and recopy to front of array
        if (n == q.length)
           resize(2*q.length); // double size of array if necessary
        q[last++] = item;      // add item
        if (last == q.length)
           last = 0;           // wrap-around
        n++;
      }

      /**
       * Removes and returns the item on this queue that was least recently added.
       * @return the item on this queue that was least recently added
       * @throws java.util.NoSuchElementException if this queue is empty
       */

       public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = q[first];
        q[first] = null;          // to avoid loitering
        n--;
        first++;
        if (first == q.length)
           first = 0;
        // shrink size of array if necessary
        if (n > 0 && n == q.length/4) 
           resize(q.length/2);
        return item;
       }

       /**
        * Returns the item least recently added to this queue.
        * @return the item least recently added to this queue
        * @throws java.util.NoSuchElementException if this queue is empty
        */
        public Item peek() {
            if (isEmpty())
               throw new NoSuchElementException("Queue underflow");
            return q[first];
        }
        
    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * 
     * @returns an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator() {
        // TODO Auto-generated method stub
        return new ArrayIterator();
    }

    // an array iteartor, from first to last-1
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext())
               throw new NoSuchElementException();
            Item item = q[(i + first) % q.length];
            i++;
            return item;
        }
    }

    /**
     * Unit tests the {@code ResizingArrayQueue} data type.
     * 
     * @param args the command-line arguments
    */
    public static void main(String[] args) {
        ResizingArrayQueue<String> queue = new ResizingArrayQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) 
               queue.enqueue(item);
            else if (!queue.isEmpty())
               StdOut.print(queue.dequeue() + " ");
        }
        StdOut.println("(" + queue.size() + " left on queue");
    }
}
