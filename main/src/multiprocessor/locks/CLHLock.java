package multiprocessor.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CLHLock implements Lock {



    AtomicReference<QNode1>  tail = new AtomicReference<>();

    ThreadLocal<QNode1> curr = new ThreadLocal<>();


    @Override
    public void lock() {

        QNode1 qnode = new QNode1();
        qnode.locked=true;
        QNode1 prev = tail.getAndSet(qnode);
        qnode.prev=prev;

        curr.set(qnode);

        if (prev==null) // no previous, so nothing to lock.
        {
            return;
        }

        while (prev.locked) {

            if (prev.abandoned)
            {
                prev=prev.prev;
                if (prev==null)  // no more nodes waiting , acquire the lock.
                    break;
            }
        }

    }



    @Override
    public void unlock() {

        QNode1 qnode = curr.get();
        qnode.locked=false;



    }




    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        QNode1 qnode = new QNode1();
        qnode.locked=true;
        QNode1 prev = tail.getAndSet(qnode);
        qnode.prev=prev;

        curr.set(qnode);

        if (prev==null) // no previous, so nothing to lock.
        {
            return true;
        }

        while(prev.abandoned)
            prev = prev.prev;

        if(prev.locked)
        {
            qnode.abandoned=true;
            return false;
        }
        else
        {
            return true;
        }

    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {


       long start = System.currentTimeMillis();
        long end = start+l ;

      /*  while(!tryLock()) {

            if (end>=System.currentTimeMillis())
                return false;

        }



        return true;*/





      //  System.out.println(end + " " + start);

        while(end>System.currentTimeMillis())
        {
            if (tryLock()) {
               //System.out.println("Got lock");
                return true;
            }
        }

      //  System.out.println(Thread.currentThread().getName() + " did not acquire lock");

        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
