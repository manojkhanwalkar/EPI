package java891011121314.fjframework;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FJ<T> {


    interface LB {

        int next();

    }


    final static class RR implements LB
    {
        int next;
        int max;
        public RR(int num)
        {
            this.max = num;
        }

        @Override
        public synchronized int next()
        {
            if (next==max)
            {
                next=0;
                return next;
            }
            else
            {
                return next++;
            }
        }
    }



   /* final static class LL implements LB
    {
        int next;
        int max;
        public LL(int num)
        {
            this.max = num;
        }


        @Override
        public synchronized int next()
        {
            if (next==max)
            {
                next=0;
                return next;
            }
            else
            {
                return next++;
            }
        }
    }*/


    BlockingQueue<RT<T>>[] queues ;

    LB lb ;

    Thread[] threads ;

    public FJ()
    {
        int num = 10* Runtime.getRuntime().availableProcessors();
        queues = (BlockingQueue<RT<T>>[] ) new LinkedBlockingQueue<?>[num];


        lb = new RR(num);
        threads = new Thread[num];

        for (int i=0;i<num;i++)
        {
            queues[i] = new LinkedBlockingQueue<>();
        }

        for (int i=0;i<num;i++)
        {
            threads[i] = new Thread(new RTWrapper<>(queues[i],this));
            threads[i].start();
        }

    }


    static class RTWrapper<T> implements Runnable
    {
        BlockingQueue<RT<T>> queue;
        FJ<T> fjpool ;
        public RTWrapper(BlockingQueue<RT<T>> queue, FJ<T> fjpool)
        {
            this.queue = queue;
            this.fjpool = fjpool;
        }

        public void run()
        {
            RT.fjhandle.set(fjpool);

            while(true)
            {
                try {
                    RT<T> task = queue.take();
                    T result = task.compute();
                    synchronized (task) {
                        task.result = result;
                        task.completed=true;
                        System.out.println("Completed" + task);
                        task.notifyAll();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  T invoke(RT<T> task)
    {
       // task.fjhandle = this;
        int i = lb.next();

        task.forked=true;

        try {
            queues[i].put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return task.join();
    }


    public  void  fork(RT<T> task)
    {
      //  task.fjhandle = this;
        int i = lb.next();

        task.forked=true;

        try {
            queues[i].put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
