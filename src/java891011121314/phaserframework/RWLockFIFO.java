package java891011121314.phaserframework;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class RWLockFIFO extends RWLock {

    protected RWLockFIFO() {}

    int readers=0;

    long ownerThread;

    int refCount=0;

    boolean wlocked = false;

    Queue<LockTask> tasks ;

    Comparator<LockTask> readPriority = (t1,t2)-> {
        if (t1.type== LockTask.Type.Reader && t2.type== LockTask.Type.Writer)
        {
            return -1;
        }
        if (t2.type== LockTask.Type.Reader && t1.type== LockTask.Type.Writer)
        {
            return 1;
        }

        // both are same types compare via times
        if (t1.time<t1.time)
            return -1;
        else
            return 1;


    };


    public RWLockFIFO(PriorityStrategy strategy)
    {
        switch(strategy)
        {
            case FIFO :
                tasks  = new ArrayDeque<>();
                break;

            case READ:
                tasks = new PriorityQueue<>(readPriority);
                break;

            case WRITE:
                tasks = new PriorityQueue<>(readPriority.reversed());
                break;

        }
    }


    public synchronized void rlock()
    {
        LockTask lockTask = new LockTask(LockTask.Type.Reader);
        tasks.add(lockTask);

        if (!wlocked&& tasks.size()==1)
            lockTask.makeReady();

        // if wlocked nothing to do but wait
        while(wlocked && !lockTask.ready) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        refCount++;

    }

    public synchronized void wlock()
    {
        LockTask lockTask = new LockTask(LockTask.Type.Writer);
        tasks.add(lockTask);
        if (!wlocked&& tasks.size()==1)
            lockTask.makeReady();


        while(wlocked && refCount!=0 && !lockTask.ready)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        wlocked = true;

    }

    public synchronized void runlock()
    {
        refCount--;
        if (refCount==0)
        {
            unlock();
        }
    }

    private synchronized void unlock()
    {
        // check queue - if first writer then make that ready. if first reader , then make all readers ready till you encounter a writer or queue is empty.
        LockTask first = tasks.peek();
        if (first.type== LockTask.Type.Writer)
        {
            tasks.remove();
            first.makeReady();
        }
        else
        {
            while(!tasks.isEmpty())
            {
                first = tasks.peek();
                if (first.type== LockTask.Type.Writer)
                    break;
                first.makeReady();
                tasks.remove();
            }
        }

        notifyAll();

    }


    public synchronized  void wunlock()
    {
        wlocked=false;
        unlock();
    }




}
