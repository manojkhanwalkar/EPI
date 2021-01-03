package multiprocessor.work;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class UBDEQueue {

    volatile int bottom =0;
    AtomicInteger top = new AtomicInteger(0);
    volatile CircularArray tasks;


    public UBDEQueue()
    {
        tasks = new CircularArray(5);
    }


    // used to see if other threads can take work.
    public boolean isEmpty()
    {
        int lTop = top.get();
        int lBottom = bottom;

        return (lBottom<=lTop);
    }

    // only one thread pops from the bottom
    public UBDERA popBottom()
    {

        bottom--;

        int oldTop = top.get();

        int newTop = oldTop+1;

        int size = bottom-oldTop;

        if (size<0)
        {
            bottom=oldTop;
            return null;
        }

        UBDERA action = tasks.get(bottom);
        if (size>0)
            return action;

        if (!top.compareAndSet(oldTop,newTop))
        {
            action = null;

        }
        bottom = oldTop+1;
        return action;

    }


    // only one thread adds to the bottom.
    public void pushBottom(UBDERA r)
    {

        CircularArray currentTasks = tasks;
        int oldBottom = bottom;
        int oldTop = top.get();

        int size = oldBottom-oldTop;

        if (size > currentTasks.capacity-1)
        {
            currentTasks = currentTasks.resize(oldTop,oldBottom);
            tasks = currentTasks;
        }

        tasks.put(oldBottom,r);
        bottom++;
    }



    // muitiple threads pop from the top.
    public UBDERA  popTop()
    {
        int oldBottom = bottom;
        int oldTop = top.get();

        int size = oldBottom-oldTop;

        if (size < 0)
            return null;


        UBDERA r = tasks.get(oldTop);

        if (top.compareAndSet(oldTop,oldTop+1))
        {
            return r;
        }

        return null;



    }




}
