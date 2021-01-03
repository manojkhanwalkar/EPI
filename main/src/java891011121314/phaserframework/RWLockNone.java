package java891011121314.phaserframework;

public class RWLockNone extends RWLock {

    protected RWLockNone() {}

    int readers=0;

    long ownerThread;

    int refCount=0;

    boolean wlocked = false;


    public synchronized void rlock()
    {
        while(wlocked) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        refCount++;

    }

    public synchronized void wlock()
    {
        while(wlocked && refCount!=0)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        wlocked = true;

    }

    public synchronized void runlock()
    {
        refCount--;
        if (refCount==0)
        {
            notifyAll();
        }
    }


    public synchronized  void wunlock()
    {
        wlocked=false;
        notifyAll();
    }




}
