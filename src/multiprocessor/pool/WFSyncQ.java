package multiprocessor.pool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WFSyncQ<T> implements Pool<T> {

    // only one thread can put at a time . only one can read. the thread sending the message waits till is has been taken .

    private AtomicReference<T> item = new AtomicReference<>(null);


    private AtomicBoolean enqueing = new AtomicBoolean(false);




    @Override
    public T get() {

            while(true)
            {


                T value = item.get();
                if (value!=null)
                {
                    if (item.compareAndSet(value,null))
                        return value;
                }


            }




    }

    @Override
    public void put(T t) {

        while(true)
        {
            boolean r = enqueing.get();
            if (!r)
            {
                if (enqueing.compareAndSet(false,true))
                {
                    // producer now sets the item and waits for item to be null before returning
                    item.set(t);
                    while(true)
                    {

                        T value = item.get();
                        if (value==null)
                        {
                            enqueing.set(false);
                            return;
                        }


                    }
                }
            }
        }


    }
}
