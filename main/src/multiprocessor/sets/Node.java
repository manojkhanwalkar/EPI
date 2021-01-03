package multiprocessor.sets;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node<T> {

    T item;

    int key;

    AtomicMarkableReference<Node<T>> next;

    volatile boolean marked = false;

    public Node()
    {

    }

    public Node(T t) {

        item = t;
    }


    Lock lock = new ReentrantLock();

    public void lock()
    {
        lock.lock();
    }


    public void unlock()
    {
        lock.unlock();
    }
}
