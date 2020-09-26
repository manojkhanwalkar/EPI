package multiprocessor.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TTASLock implements Lock {


    AtomicBoolean state = new AtomicBoolean(false);

    int min , max;

    int curr;

    public TTASLock(int min, int max)
    {
        this.min = min;
        this.max = max;
        this.curr=min;
    }

    private void backoff()
    {
        System.out.println(Thread.currentThread().getName() + " is backing off for " + curr);

        try {
            Thread.sleep(curr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        curr = Math.min((int)Math.round((curr*1.2)),max);

        curr = curr==max?min:curr;


    }

    @Override
    public void lock() {

        // change from false to true.

        while(true) {

            while (state.get()) { }

            if (!state.getAndSet(true)) {

                return;

            }
            else
            {
                backoff();
            }


        }



    }


    @Override
    public void unlock() {

        state.set(false);
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
