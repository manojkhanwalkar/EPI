package java891011121314.phaserframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEven {


    static final int ODD = 1;
    static final int EVEN = 2;
    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();

        IntegerWrapper integerWrapper = new IntegerWrapper();
        Task even = new Task(EVEN,integerWrapper);
        Task odd = new Task(ODD,integerWrapper);

        service.submit(even);
        service.submit(odd);


    }

    static class IntegerWrapper
    {
        int count=0;

        Lock lock = new ReentrantLock();

        Condition evenodd ;
        public  IntegerWrapper()
        {
            evenodd = lock.newCondition();
        }

        public int get()
        {
            return count;
        }

        public void increment()
        {
            count++;
        }
    }



    static class Task implements Runnable{

        final int type;

        IntegerWrapper integerWrapper;

        public Task(int type, IntegerWrapper integerWrapper)
        {
            this.type=type;
            this.integerWrapper = integerWrapper;
        }

        private void process(int x)
        {
            System.out.println(Thread.currentThread().getName() + " " + x);
            integerWrapper.increment();
            integerWrapper.evenodd.signalAll();

        }

        private void waitFor()
        {
            try {
                integerWrapper.evenodd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void run()
        {
            while(true)
            {
                integerWrapper.lock.lock();
                int x = integerWrapper.count;
                if (x>100) {
                    integerWrapper.lock.unlock();
                    break;
                }
                if (type==EVEN) {
                    if (x % 2 == 0) {
                        process(x);

                    } else {

                        waitFor();

                    }
                }
                else {
                    if (x%2!=0) {  // type ==ODD

                        process(x);

                    }
                    else
                    {
                        waitFor();
                    }
                }

                integerWrapper.lock.unlock();

            }

        }
    }

}
