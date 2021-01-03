package multiprocessor.locks;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CompositeLock implements Lock {



   //AtomicStampedReference<QNode> tail = new AtomicStampedReference<>(null,0);

    AtomicReference<QNode> tail = new AtomicReference<>();

    ThreadLocal<QNode> curr = new ThreadLocal<>();

    volatile QNode[] waiting;

    public final static int WAIT_SIZE = 3;  // just 3 threads will spin in the queue for locking .

    public CompositeLock()
    {
        waiting = new QNode[WAIT_SIZE];

        for (int i=0;i<WAIT_SIZE;i++)
        {
            waiting[i] = new QNode();
        }
    }

    Random random = new Random();

    private QNode fastPath = new QNode();

    private boolean fastPath()
    {
        if (!fastPath.state.compareAndSet(QNode.State.FREE, QNode.State.LOCKED))

            return false;

        if (tail.compareAndSet(null,fastPath)) {

            System.out.println(Thread.currentThread().getName() + " Acquired FASTPATH Lock " + fastPath);

            curr.set(fastPath);

            return true;
        }
        else {

            fastPath.state.set(QNode.State.FREE);
            return false;
        }

    }

    private QNode acquireNode()
    {
        while(true)
        {
            int num = random.nextInt(WAIT_SIZE);

            QNode node = waiting[num];

            if (node.state.compareAndSet(QNode.State.FREE, QNode.State.LOCKED))
            {
                return node;
            }

            backoff(); // node not free so backoff for some time before trying.

        }
    }

    private int sleep = 100;

    private void backoff()
    {
         // System.out.println(Thread.currentThread().getName() + " is backing off " );

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    private void insertIntoLockingQueue(QNode node)
    {

      //  int currStamp = tail.getStamp();

      //  QNode prev = tail.get(currStamp);


        //tail.compareAndSet(prev,node,0,1);


         QNode prev = tail.getAndSet(node);
        node.prev=prev;

    }

    private void waitForTurn(QNode node)
    {

        QNode prev = node.prev;
        if (prev==null) // no previous, so nothing to wait.
        {
            return;
        }

        System.out.println(Thread.currentThread().getName() + "waiting for " + prev + " holding " + node);

        while (prev.state.get()== QNode.State.LOCKED) {

          /*  if (prev.state.get()== QNode.State.ABORTED)
            {
                QNode tmp = prev;

                prev = prev.prev;

                tmp.state.set(QNode.State.FREE);

                if (prev==null)
                    return;
            }*/
        }

        prev.state.set(QNode.State.FREE);


    }


    @Override
    public void lock() {

        // fast path
        if (fastPath())
            return;

        // 3 steps - aqcuire node from array or wait
        // queue up for locking
        // qcuire the lock when pred is done

        QNode qnode = acquireNode();
        insertIntoLockingQueue(qnode);

        waitForTurn(qnode);

        curr.set(qnode);

        System.out.println(Thread.currentThread().getName() + " Acquired Lock " + qnode);



    }



    @Override
    public void unlock() {

        QNode qnode = curr.get();

        if (tail.compareAndSet(qnode,null))
        {
            qnode.state.set(QNode.State.FREE);
        }
        else {

            qnode.state.set(QNode.State.RELEASED);
        }

       // curr.set(null);  // remove from threads local storage

        System.out.println(Thread.currentThread().getName() + " Released Lock " + qnode);



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
