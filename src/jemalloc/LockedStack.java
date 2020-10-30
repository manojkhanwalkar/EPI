package jemalloc;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedStack<E> {

    Stack<E> stack = new Stack();

    Lock lock = new ReentrantLock();

    Condition notempty = lock.newCondition();


    int count ;

    int size;
    public LockedStack(int size)
    {
        this.size = size;


    }


    public void put(E e)
    {
        lock.lock();
        stack.push(e);
        count++;
        notempty.signalAll();
        lock.unlock();
    }


    public E pop()
    {
        try {
            lock.lock();
            while(count<=0)
            {
                try {
                    notempty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            count--;

            return stack.pop();

        } finally {

            lock.unlock();

        }
    }

}
