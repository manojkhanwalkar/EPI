package jemalloc1;

import java.util.BitSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBuffer{



    Lock lock = new ReentrantLock();

    Condition notempty = lock.newCondition();

    char[] buffer;

    BitSet bits ;

    int bufSize ;

    int elements;

    public LockedBuffer(int bufSize, int elements)
    {

        bits = new BitSet(elements);
        buffer = new char[bufSize*elements];
        this.bufSize = bufSize;
        this.elements = elements;

        for (int i=0;i<elements;i++)
            bits.set(i,true);
    }


    public void put(Buffer buf)
    {
        lock.lock();
        int index = buf.start/bufSize;
        bits.set(index);
        notempty.signalAll();
        lock.unlock();
    }


    public Buffer pop()
    {
        try {
            lock.lock();
            while(bits.isEmpty())
            {
                try {
                    notempty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // get non empty bit
            for (int i=0;i<elements;i++)
            {
                if (bits.get(i))
                {
                    int start = i*bufSize;
                    int end = start+bufSize;
                    bits.set(i,false);
                    var buf =  new Buffer(start,end,buffer);
                    buf.threadIdOfAllocation = Thread.currentThread().getId();

                    return buf;
                }
            }

            throw new RuntimeException("Unknown error ") ;

        } finally {

            lock.unlock();

        }
    }

}
