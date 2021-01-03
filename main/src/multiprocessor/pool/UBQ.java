package multiprocessor.pool;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UBQ<T> implements Pool<T> {

    static class Node<T>
    {
       T value;

       Node<T> next;

       public Node(T value)
       {
           this.value = value;
       }
    }

    Node<T> head ;
    Node<T> tail;

    Lock enqLock = new ReentrantLock();
    Lock deLock = new ReentrantLock();

    public UBQ()
    {
        head = new Node<>(null);  // sentinel node
        tail = head;
    }

    @Override
    public T get() {

        try{
            deLock.lock();

            Node<T> result = head.next;
            if (result!=null)
            {
                head = head.next;
                return result.value;
            }
            else
            {
                return null;
            }

        }finally {
            deLock.unlock();
        }

    }

    @Override
    public void put(T value) {
        Node<T> node = new Node<>(value);

        try {
            enqLock.lock();

            tail.next = node;
            tail = tail.next;

        } finally
        {
            enqLock.unlock();
        }

    }
}
