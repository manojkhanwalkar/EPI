package java891011121314.phaserframework;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BQ<T> {


    public static <T> BQ<T> getFixedCapacityQueue(int capacity)
    {
        return new BQ(capacity);
    }

    public static <T>  BQ<T> getFixedCapacityQueue()
    {
        return new BQ();
    }


    private BQ()
    {
        queue = new ArrayDeque<>();
        unLimitedCapacity=true;
        init();
        capacity = -1;
    }

    boolean unLimitedCapacity;
    final int capacity;

    int curr=0;

    private BQ(int capacity)
    {
        queue= new ArrayDeque<>(capacity);
        unLimitedCapacity=false;
        this.capacity=capacity;
        init();
    }


    private void init()
    {
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }


    Queue<T> queue ;

    Lock lock = new ReentrantLock();
    Condition notFull;
    Condition notEmpty;


    public T get()
    {
        try {
            lock.lock();
            if (queue.isEmpty())
                notEmpty.await();
            T t = queue.remove();
            curr--;
            return t;
        } catch (Exception ex) {ex.printStackTrace();
        } finally {
            notFull.signalAll();
            lock.unlock();
        }

        return null;
    }

    public void put(T t)
    {
        try {
            lock.lock();
            if (!unLimitedCapacity && curr==capacity) {
                notFull.await();
            }
            queue.add(t);
            curr++;
        } catch (Exception ex) {ex.printStackTrace();
        } finally {
            notEmpty.signalAll();
            lock.unlock();
        }

    }



}
