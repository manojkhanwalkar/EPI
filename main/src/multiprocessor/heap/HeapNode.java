package multiprocessor.heap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HeapNode<T> implements Comparable<HeapNode<T>>{

    T item;

    int score;

    Lock lock = new ReentrantLock();

    public HeapNode(T item , int score)
    {
        this.item = item;
        this.score = score;
    }

    public int compareTo(HeapNode<T> other)
    {
        return Integer.compare(this.score, other.score);
    }


    @Override
    public String toString() {
        return "HeapNode{" +
                "score=" + score +
                '}';
    }


    public void lock()
    {
        lock.lock();
    }


    public void unlock()
    {
        lock.unlock();
    }
}
