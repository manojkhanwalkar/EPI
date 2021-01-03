package java891011121314.hashes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CGHashSet<T> extends BaseHashSet<T> {


    Lock lock = new ReentrantLock();

    public CGHashSet(int capacity)
    {
        super(capacity);
    }

    @Override
    public void acquire(T t) {

        lock.lock();
    }

    @Override
    public void release(T t) {

        lock.unlock();

    }

    @Override
    protected boolean policy() {

        if (size/elements.length>10)
            return true;
        else
            return false;
    }

    @Override
    protected void resize() {

        try {
            acquire(null);

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
            release(null);
        }

    }
}
