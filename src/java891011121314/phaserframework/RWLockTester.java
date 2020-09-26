package java891011121314.phaserframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RWLockTester {


    public static void main(String[] args) {

        RWLockTester tester = new RWLockTester();

       // tester.testNone();

       // tester.testFIFO();

        //tester.testReader();

        tester.testWriter();
    }


    public void testNone()
    {
        test(RWLock.PriorityStrategy.None);
    }

    public void testFIFO()
    {
        test(RWLock.PriorityStrategy.FIFO);
    }

    public void testReader()
    {
        test(RWLock.PriorityStrategy.READ);
    }

    public void testWriter()
    {
        test(RWLock.PriorityStrategy.WRITE);
    }



    ExecutorService service = Executors.newCachedThreadPool();
    private void test(RWLock.PriorityStrategy strategy)
    {
        RWLock lock = RWLock.getLock(strategy);
        State state = new State();
        Reader reader1 = new Reader(state,lock);
        Reader reader2 = new Reader(state,lock);
        Writer writer1 = new Writer(state,lock);
        Writer writer2 = new Writer(state,lock);

        service.submit(reader1);
        service.submit(reader2);
        service.submit(writer1);
        service.submit(writer2);
    }

    static class State {

        volatile int counter;

        public int getCounter() {
            return counter;
        }

        public void incermentCounter()
        {
            counter++;
        }
    }

    static class Reader implements Runnable
    {
        State state ;
        RWLock lock;
        public Reader(State state, RWLock lock)
        {
            this.state = state;
            this.lock = lock;
        }

        public void run()
        {
            for (int i=0;i<10;i++) {
                try {
                    lock.rlock();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "Reader , Counter state is " + state.getCounter());
                    lock.runlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    static class Writer implements Runnable
    {
        State state ;
        RWLock lock;
        public Writer(State state, RWLock lock)
        {
            this.state = state;
            this.lock = lock;
        }

        public void run()
        {
            for (int i=0;i<10;i++) {
                try {
                    lock.wlock();
                    Thread.sleep(1000);
                    state.incermentCounter();
                    System.out.println(Thread.currentThread().getName() + "Writer , Counter state is " + state.getCounter());
                    lock.wunlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
