package bin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BinCache {

    Map<String,BinRecord> cache = new HashMap<>();

    ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock readLock ;

    Lock writeLock;

    public BinCache()
    {
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }


    public BinRecord get(String pan)
    {
        String bin = pan.substring(0,6);

        try {
            readLock.lock();
            return cache.get(bin);

        } finally {
            readLock.unlock();
        }
    }




    public void refresh(Map<String,BinRecord> cache1)
    {
        writeLock.lock();
        cache = cache1;
        writeLock.unlock();
    }


    @Override
    public String toString() {
        return "BinCache{" +
                "cache=" + cache +
                '}';
    }
}
