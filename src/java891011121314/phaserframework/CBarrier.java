package java891011121314.phaserframework;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class CBarrier {

    AtomicInteger subscribers;

    AtomicInteger waiters = new AtomicInteger();

    public CBarrier(int parties)
    {
        subscribers = new AtomicInteger(parties);
    }



    public synchronized  void await()
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

                waiters.set(0);
                notifyAll();
            }


    }








}
