package multiprocessor.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node1<T> {

    T item;

    int key;

    Node1<T> next;

    volatile boolean marked = false;

    public Node1()
    {

    }

    public Node1(T t) {

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
