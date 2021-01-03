package multiprocessor.pool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PoolTester {


    public static void main(String[] args) {

        PoolTester poolTester = new PoolTester();

        poolTester.testQueue();


    }

    ExecutorService service = Executors.newFixedThreadPool(10);

    ExecutorService service1= Executors.newFixedThreadPool(10);

    public void testQueue() {
//        Pool<String> pool = new UBQ<>();
 //       Pool<String> pool = new SyncQ<>();

//        Pool<String> pool = new WFUBQ<>();

   //     Pool<String> pool = new WFSyncQ<>();

   //     Pool<String> pool = new WFSUBQ<>();

        Pool<String> pool = new SyncDualQ<>();




        int start = 0;


        for (int i=0;i<500;i++)
        {
            service.submit(new Inserter(start,++start,pool));
            //service.submit(new Dequeuer(pool));


        }

     /*   try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<5;i++)
        {
         //   service.submit(new Inserter(start,end,pool));
            service.submit(new Dequeuer(pool));
        //    start=end;
        //    end+=100;
        }
*/



        for (int i=0;i<5;i++)
        {
            //   service.submit(new Inserter(start,end,pool));
            service1.submit(new Dequeuer(pool));
            //    start=end;
            //    end+=100;
        }

    }

    static class Inserter implements Runnable {
        int start;
        int end;
        Pool<String> pool;

        public Inserter(int start, int end, Pool<String> pool) {
            this.start = start;
            this.end = end;
            this.pool = pool;
        }


        public void run() {
            String s = "Enqueued by " + Thread.currentThread().getName() + " ";

            for (int i = start; i < end; i++) {
                pool.put(s+i);

              /*  try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }

        }

    }


    static class Dequeuer implements Runnable
    {
        Pool<String> pool;
        public Dequeuer( Pool<String> pool)
        {

            this.pool =pool;
        }


        public void run()
        {
            String s = "Dequeued by " + Thread.currentThread().getName();

            while(true)
            {

                String res = pool.get();
                if (res!=null)
                {
                    System.out.println(res + " " + s);
                }
                else
                {


                  /*  try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }


        }

    }


}



