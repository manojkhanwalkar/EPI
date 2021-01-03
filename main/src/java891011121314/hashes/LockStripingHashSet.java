package java891011121314.hashes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStripingHashSet<T> extends BaseHashSet<T> {


    Lock[] locks ;



    public LockStripingHashSet(int capacity, int stripe)
    {
        super(capacity);

        locks = new Lock[stripe];

        for (int i=0;i<locks.length;i++)
        {
            locks[i] = new ReentrantLock();
        }
    }

    @Override
    public void acquire(T t) {

        int bucket = Math.abs(t.hashCode()%locks.length);


        locks[bucket].lock();
    }

    @Override
    public void release(T t) {

        int bucket = Math.abs(t.hashCode()%locks.length);


        locks[bucket].unlock();

    }

    @Override
    protected boolean policy() {

        if (size/elements.length>10)
            return true;
        else
            return false;
    }

    private void acquireAll()
    {
        Arrays.stream(locks).forEach(Lock::lock);
    }

    private void releaseAll()
    {
        Arrays.stream(locks).forEach(Lock::unlock);
    }

    @Override
    protected void resize() {




        try {


            acquireAll();

            List<T> [] newelements = (List<T>[])new List[elements.length*2];

            for (int i=0;i<newelements.length;i++)
            {
                newelements[i]= new ArrayList<>(); 
            }

            for (int i=0;i<elements.length;i++)
            {
                List<T> list = elements[i];
                list.stream().forEach(t->{

                    int bucket = Math.abs(t.hashCode()%newelements.length);
                    newelements[bucket].add(t);
                });
            }

            elements = newelements;

        } finally {
            releaseAll();
        }

    }
}
