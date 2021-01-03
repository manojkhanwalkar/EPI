package java891011121314.phaserframework;

import java.util.concurrent.atomic.AtomicInteger;

public class Phazer {

    AtomicInteger subscribers = new AtomicInteger();

    AtomicInteger waiters = new AtomicInteger();

    int phase;

    public void register()
    {
        subscribers.incrementAndGet();
    }


    public synchronized  void arriveAndAwaitAdvance()
    {
            waiters.incrementAndGet();
            if(waiters.get()!=subscribers.get()) {

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                phase++;
                waiters.set(0);
                notifyAll();
            }


    }



    public synchronized  void arriveAndDeregister()
    {
        subscribers.decrementAndGet();
        if(waiters.get()==subscribers.get()) {

            phase++;
            waiters.set(0);
            notifyAll();
        }





    }





}
