package multiprocessor.work;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BDEQProcessor {


    static class ThreadWrapper implements Runnable
    {
        BDEQueue wsq;
        BDEQueue[] queues;
        BDEQProcessor processor;

        int queueIndex;
        public ThreadWrapper(int queueIndex, BDEQueue[] queues, BDEQProcessor processor)
        {
            wsq = queues[queueIndex];
            this.queues = queues;
            this.queueIndex = queueIndex;
            this.processor = processor;


        }

        Random random = new Random();

        public void run()
        {
            BDERA.processor.set(processor);

            while(true)
            {


                BDERA node = wsq.popBottom();
                if (node==null)
                {
                    int index = random.nextInt(queues.length);
                    if (index!=queueIndex)
                    {
                        node = queues[index].popTop();
                    }
                }

                if (node!=null)
                {
                    node.compute();
                }


            }



        }
    }

    int num;

    BDEQueue[] queues ;

    Thread[] threads;

    Map<String,BDEQueue> threadtoQueueMap = new HashMap<>();


    public BDEQProcessor(int num)
    {
        this.num=num;

        queues = new BDEQueue[num];

        threads = new Thread[num];

        for (int i=0;i<num;i++)
        {
            queues[i] = new BDEQueue(100);
        }

        for (int i=0;i<num;i++)
        {
            threads[i] = new Thread(new ThreadWrapper(i,queues,this));

            threadtoQueueMap.put(threads[i].getName(),queues[i]);
        }




    }


    public void submit(BDERA action)
    {
        // check thread on which it was created and put in that queue.

        BDEQueue queue = threadtoQueueMap.get(action.created);
        queue.pushBottom(action);

    }

    public void start(BDERA action)
    {
        queues[0].pushBottom(action);


        for (int i=0;i<num;i++)
            threads[i].start();
    }



    public static void main(String[] args) {

        BDEQProcessor processor = new BDEQProcessor(5);

        processor.start(new BDEQRecursiveAction());



    }


}
