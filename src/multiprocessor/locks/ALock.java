package multiprocessor.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ALock implements Lock {


    static ThreadLocal<Integer> localSlot = new ThreadLocal<>();

    AtomicInteger tail = new AtomicInteger(0);

    volatile boolean[] slot ;  // if the threads slot is true , it can proceed .

    int capacity;

    public ALock(int capacity)
    {
        this.capacity=capacity;
        slot = new boolean[capacity];

        slot[0] = true;
    }

    @Override
    public void lock() {
        // find your slot

        int myslot = tail.getAndIncrement()%capacity;
        while(!slot[myslot]) {} // wait till the slot is set to true;
        localSlot.set(myslot);


    }


    @Override
    public void unlock() {

        int myslot = localSlot.get();

        slot[myslot] = false;

        slot[(myslot+1)%capacity] = true;  // flag next thread that can acquire the lock.

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }
}
