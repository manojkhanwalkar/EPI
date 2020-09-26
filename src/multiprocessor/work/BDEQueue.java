package multiprocessor.work;

import java.util.concurrent.atomic.AtomicStampedReference;

public class BDEQueue {


    BDERA[] tasks ;

    volatile int bottom =0;

    AtomicStampedReference<Integer> top = new AtomicStampedReference<>(0,0);

    int capacity ;
    public BDEQueue(int capacity)
    {
        this.capacity=capacity;

        tasks = new BDERA[capacity];
    }


    // used to see if other threads can take work.
    public boolean isEmpty()
    {
        int lTop = top.getReference();
        int lBottom = bottom;

        return (lBottom<=lTop);
    }

    // only one thread pops from the bottom
    public BDERA popBottom()
    {
        if (bottom==0)
            return null;

        bottom--;

        BDERA r = tasks[bottom];
        int[] stamp = new int[1];
        int top1= top.get(stamp);
        int oldStamp = stamp[0];
        int newStamp = oldStamp+1;
        int newTop = 0;


        if (bottom>top1)
        {
            return r;
        }

        if (bottom==top1)
        {
            bottom=0;
            if (top.compareAndSet(top1,newTop,oldStamp,newStamp))
                return r;
        }

        top.set(0,newStamp);
        return null;
    }


    // only one thread adds to the bottom.
    public void pushBottom(BDERA r)
    {
        tasks[bottom] = r;
        bottom++;
    }



    // muitiple threads pop from the top.
    public BDERA  popTop()
    {
        int[] stamp = new int[1];
        int top1= top.get(stamp);
        int oldStamp = stamp[0];
        int newStamp = oldStamp+1;
        int newTop = top1+1;

        if (bottom<=top1)
            return null;

        BDERA r = tasks[top1];

        if (top.compareAndSet(top1,newTop,oldStamp,newStamp))
            return r;

        return null;


    }




}
