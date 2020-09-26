package java891011121314.hashes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RefinableHashSet<T> extends BaseHashSet<T> {


    Lock[] locks ;


    AtomicMarkableReference<Thread> owner = new AtomicMarkableReference<>(null,false);


    //TODO - add ability to resize the data set using lockfree mechanism ......

    private void initializeLocks(int lockCapacity)
    {
        locks = new Lock[lockCapacity];

        for (int i=0;i<locks.length;i++)
        {
            locks[i] = new ReentrantLock();
        }
    }

    public RefinableHashSet(int capacity, int stripe)
    {
        super(capacity);

        initializeLocks(stripe);


    }

    @Override
    public void acquire(T t) {
        boolean[] mark = {false};

        while(true)
        {
            owner.get(mark);
            if (mark[0]==false)
                break;
        }  // wait if some thread is resizing . resizing thread cannot acquire while resizing .


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

        // wait for all locks to become unlocked . No need to lock them as the markable reference will hold other threads

        Arrays.stream(locks).forEach(lock->{

            ReentrantLock reentrantLock = (ReentrantLock)lock;
            while(reentrantLock.isLocked());

            });
    }

    private void releaseAll() {
    }

    @Override
    protected void resize() {

        Thread thread = null;

        try {

            int oldCapacity = elements.length;
            boolean[] mark = {false};
             thread = Thread.currentThread();

            boolean result= owner.compareAndSet(null,thread,false,true);
            if (!result) // some other thread is resizing , return without changes
                return;

            if (oldCapacity!=elements.length)// someone resized before us
                return ;

            int capacity = 2*oldCapacity;

            acquireAll();

            List<T> [] newelements = (List<T>[])new List[capacity];

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

            int lockCapacity = 2*locks.length;

            initializeLocks(lockCapacity);

        } finally {
           owner.compareAndSet(thread,null,true,false);
        }

    }
}
