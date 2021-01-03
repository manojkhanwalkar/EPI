package java891011121314.phaserframework;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReLock implements Lock {




    @Override
    public void lockInterruptibly() throws InterruptedException {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }


    long ownerThread;

    int refCount=0;

    boolean locked = false;


    @Override
    public synchronized void lock() {

        long requestingThread = Thread.currentThread().getId();


        if (locked && requestingThread==ownerThread)
        {
            refCount++;
            return;
        }

        // locked by another thread.

        while(locked)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


            ownerThread = requestingThread;
            refCount++;
            locked = true;
            return;




    }


    @Override
    public synchronized void unlock() {

        long requestingThread = Thread.currentThread().getId();
        if (!locked)
            return;

        if (ownerThread==requestingThread) {
            refCount--;
            if (refCount==0)
            {
                locked=false;
                ownerThread=0;
                notifyAll();
            }
        }
        else
        {
            throw new RuntimeException("Lock not owned by thread " + requestingThread + " lock is owned by " + ownerThread);
        }

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
