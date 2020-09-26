package multiprocessor.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class LockTester {


    public static void main(String[] args) {

        LockTester tester = new LockTester();

        tester.testTASLock();


    }

    ExecutorService service = Executors.newFixedThreadPool(50);

    int count = 0;

    public int incrementCount()
    {
        return count++;
    }




    public void testTASLock()
    {
        Lock lock = new CompositeLock();
        for (int i=0;i<50;i++)
        {

            service.submit(()->{

                int local = 0;

                while(local<1000) {

          /*          while (true) {
                        try {
                            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                    }*/

               //  while(!lock.tryLock()) {}

                        lock.lock();

                        local = incrementCount();

                        if (local<1000) {

                            System.out.println(Thread.currentThread().getName() + " " + local);

                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        lock.unlock();
                    }

            });
        }


    }
}
