package java891011121314.phaserframework;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static java891011121314.phaserframework.RWLock.PriorityStrategy.None;


// not reentrant

public abstract class RWLock  {

    public enum PriorityStrategy { None, FIFO , READ, WRITE }


    public static RWLock getLock(PriorityStrategy strategy)
    {
        switch (strategy)
        {
            case None :

                return new RWLockNone();

            case FIFO:

                return new RWLockFIFO(PriorityStrategy.FIFO);

            case READ:

                return new RWLockFIFO(PriorityStrategy.READ);

            case WRITE:

                return new RWLockFIFO(PriorityStrategy.WRITE);


            default:
                    throw new UnsupportedOperationException();

        }


    }



    public abstract void rlock();
    public abstract void wlock();
    public abstract void runlock();
    public abstract void wunlock();

    static class LockTask
    {
        public enum Type { Reader,Writer}

        boolean ready = false;

        Type type;

        long time ;
        public LockTask(Type type)
        {
            this.type = type;

            time = System.nanoTime();
        }

        public void makeReady()
        {
            ready = true;
        }


    }




}
