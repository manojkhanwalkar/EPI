package multiprocessor.pool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncQ<T> implements Pool<T> {

    // only one thread can put at a time . only one can read. the thread sending the message waits till is has been taken .

   private  T item = null;
    private boolean enqueing=false;

    private Lock lock = new ReentrantLock();

    private Condition condition ;

    public SyncQ()
    {
        condition = lock.newCondition();
    }

    @Override
    public T get() {

        try {

            lock.lock();
            while (item==null)
                condition.await();

            T result = item;
            item = null;
            condition.signalAll();

            return result;

        }catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return null;


    }

    @Override
    public void put(T t) {

        try {
            lock.lock();
            while (enqueing)
                condition.await();

            enqueing=true;

            item = t;

            condition.signalAll();

            while(item!=null)
                condition.await();

            enqueing=false;

            condition.signalAll();


        } catch (Exception e) {

         }finally {
            lock.unlock();
        }

    }
}
