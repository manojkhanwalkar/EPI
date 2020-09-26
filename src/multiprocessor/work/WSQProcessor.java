package multiprocessor.work;

import java.util.Random;

public class WSQProcessor {

    static class ThreadWrapper implements Runnable
    {
        WSQ wsq;
        WSQ[] queues;
        WSQProcessor processor;

        int queueIndex;
        public ThreadWrapper(int queueIndex, WSQ[] queues, WSQProcessor processor)
        {
            wsq = queues[queueIndex];
            this.queues = queues;
            this.queueIndex = queueIndex;
            this.processor = processor;


        }

        Random random = new Random();

        public void run()
        {
            WSQRA.processor.set(processor);
            while(true)
            {
                WSQNode node = wsq.remove();
                if (node==null)
                {
                    int index = random.nextInt(queues.length);
                    if (index!=queueIndex)
                    {
                        node = queues[index].remove();
                    }
                }

                if (node!=null)
                {
                    node.process();
                }
            }



        }
    }

    int num;

    WSQ[] queues ;

    Thread[] threads;


    public WSQProcessor(int num)
    {
        this.num=num;

        queues = new WSQ[num];

        threads = new Thread[num];

        for (int i=0;i<num;i++)
        {
            queues[i] = new WSQ();
        }

        for (int i=0;i<num;i++)
        {
            threads[i] = new Thread(new ThreadWrapper(i,queues,this));
        }

        for (int i=0;i<num;i++)
            threads[i].start();

    }

    Random loadBalancer = new Random();

    /*public void submit(WSQRecursiveAction wsqTask)
    {

        WSQNode wsqNode = new WSQNode(wsqTask);
        //int index = loadBalancer.nextInt(num);

        int index = 0;
      //  wsqNode.task.submitted = String.valueOf(index);
        queues[index].add(wsqNode);

    }*/

    public void submit(WSQRA wsqTask)
    {

        WSQNode wsqNode = new WSQNode(wsqTask);


      //  int index = loadBalancer.nextInt(num);

        int index = 0;
       // wsqNode.task.submitted = String.valueOf(index);
        queues[index].add(wsqNode);

    }


    public static void main(String[] args) {

        WSQProcessor processor = new WSQProcessor(30);

        for (int i=0;i<10;i++)
        {
            processor.submit(new WSQRecursiveAction(600));
        }

    }


}
