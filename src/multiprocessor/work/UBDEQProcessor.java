package multiprocessor.work;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UBDEQProcessor {


    static class ThreadWrapper implements Runnable
    {
        UBDEQueue wsq;
        UBDEQueue[] queues;
        UBDEQProcessor processor;

        int queueIndex;
        public ThreadWrapper(int queueIndex, UBDEQueue[] queues, UBDEQProcessor processor)
        {
            wsq = queues[queueIndex];
            this.queues = queues;
            this.queueIndex = queueIndex;
            this.processor = processor;


        }

        Random random = new Random();

        public void run()
        {
            UBDERA.processor.set(processor);

            while(true)
            {


                UBDERA node = wsq.popBottom();
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

    UBDEQueue[] queues ;

    Thread[] threads;

    Map<String,UBDEQueue> threadtoQueueMap = new HashMap<>();


    public UBDEQProcessor(int num)
    {
        this.num=num;

        queues = new UBDEQueue[num];

        threads = new Thread[num];

        for (int i=0;i<num;i++)
        {
            queues[i] = new UBDEQueue();
        }

        for (int i=0;i<num;i++)
        {
            threads[i] = new Thread(new ThreadWrapper(i,queues,this));

            threadtoQueueMap.put(threads[i].getName(),queues[i]);
        }



    }


    public void submit(UBDERA action)
    {
        // check thread on which it was created and put in that queue.

        UBDEQueue queue = threadtoQueueMap.get(action.created);
        queue.pushBottom(action);

    }

    public void start(UBDERA action)
    {
        queues[0].pushBottom(action);


        for (int i=0;i<num;i++)
            threads[i].start();

    }



    public static void main(String[] args) {

        UBDEQProcessor processor = new UBDEQProcessor(1);

        processor.start(new UBDEQRecursiveAction());



    }


}
