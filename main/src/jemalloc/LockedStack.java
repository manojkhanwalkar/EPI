package jemalloc;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedStack {

    Stack<Buffer> stack = new Stack();

    Lock lock = new ReentrantLock();

    Condition notempty = lock.newCondition();


    int count ;

    int size;
    public LockedStack(int size)
    {
        this.size = size;


    }


    public void put(Buffer e)
    {
        lock.lock();
        stack.push(e);
        count++;
        notempty.signalAll();
        lock.unlock();
    }


    public Buffer pop()
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

            var buff = stack.pop();
            buff.threadIdOfAllocation = Thread.currentThread().getId();

            return stack.pop();

        } finally {

            lock.unlock();

        }
    }

}
