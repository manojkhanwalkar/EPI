package java891011121314.phaserframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BQTester {


    public static void main(String[] args) {

        BQTester tester = new BQTester();


        tester.test();
    }




    ExecutorService service = Executors.newCachedThreadPool();
    private void test()
    {

        BQ<Integer> bq = BQ.getFixedCapacityQueue(5);
        Consumer consumer1 = new Consumer(bq);
        Consumer consumer2 = new Consumer(bq);

        Producer producer1 = new Producer(bq);
        Producer producer2 = new Producer(bq);

        service.submit(consumer1);
        service.submit(consumer2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.submit(producer1);
        service.submit(producer2);
    }


    static class Consumer implements Runnable
    {
        BQ<Integer> bq;
        public Consumer(BQ<Integer> bq)
        {
            this.bq=bq;

        }

        public void run()
        {
            while(true) {
                    System.out.println(Thread.currentThread().getName() + "Reader , Counter state is " + bq.get());

            }


        }
    }

static     AtomicInteger count = new AtomicInteger();

    static class Producer implements Runnable
    {
        BQ<Integer> bq;



        public Producer(BQ<Integer> bq)
        {
            this.bq=bq;

        }

        public void run()
        {

            for (int i=0;i<100;i++) {

                    bq.put(count.incrementAndGet());


            }

        }
    }
}
