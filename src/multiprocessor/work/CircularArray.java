package multiprocessor.work;

import java.util.concurrent.atomic.AtomicStampedReference;

public class CircularArray {

    UBDERA[] tasks ;

    int capacity;

    int logCapacity;
    public CircularArray(int logCapacity)
    {
        capacity = 1<< logCapacity;
        tasks = new UBDERA[capacity];
        this.logCapacity = logCapacity;
    }


    public UBDERA get(int i)
    {
        return tasks[i%capacity];
    }

    public void put(int i, UBDERA action)
    {
         tasks[i%capacity] = action;
    }

    public CircularArray resize(int top, int bottom)
    {
        CircularArray newarray = new CircularArray(logCapacity+1);
        for (int i=top;i<bottom;i++)
        {
            newarray.put(i,this.get(i));

        }

        return newarray;
    }



}
