package java891011121314.phaserframework;

import java.util.concurrent.atomic.AtomicInteger;

public class CDLatch {





    int num;
    public CDLatch(int num)
    {
        this.num = num;
    }

    public synchronized void countDown()
    {
        num--;
        if (num==0)
            notifyAll();
    }


    public synchronized void await()
    {
        while(num!=0)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }









}
