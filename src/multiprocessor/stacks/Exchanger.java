package multiprocessor.stacks;

import java.util.concurrent.atomic.AtomicStampedReference;

public class Exchanger<T> {



    static final int EMPTY=0;
    static final int WAITING=1;
    static final int DONE=2;
    AtomicStampedReference<T>  slot = new AtomicStampedReference<>(null,EMPTY);

    static final int timeout = 100;
    public T popReserves()
    {

       // System.out.println("In pop reserves exchanger");
        // if slot is not empty , then slot is in use by someone , return null.
        // make slot to waiting and while not timeout wait for matching push
        // if slot is done , take the item and change slot to empty and return the item, else return null;
        // if unable to change to empty , then take and return the item .

        long start = System.currentTimeMillis();
        long end = start+timeout;

        if (slot.compareAndSet(null,null,EMPTY,WAITING))
        {

            while(System.currentTimeMillis() <end)
            {
                if (slot.getStamp()==DONE)
                {
                    T item = slot.getReference();
                    slot.compareAndSet(item,null,DONE,EMPTY);
                    return item;
                }
            }

            // time out , check one last time
            if (slot.compareAndSet(null,null,WAITING,EMPTY))
                return null;
            else  // a push thread must have changed state
            {
                T item = slot.getReference();
                boolean result = slot.compareAndSet(item,null,DONE,EMPTY);
                if (!result)
                     System.out.println("Slot is in an erroneous state");


                return item;
            }


        }
        else {

            return null;
        }
    }

    public boolean pushMatchesPop(T item)
    {

       // System.out.println("In push matches exchanger");

        // if slot is empty , then wait for it to become waiting , if not timeout.
        // if slot is waiting , then set item and make slot done and return true.
        // if timeout and empty , then return false.

        if (slot.compareAndSet(null,item,WAITING,DONE))
        {
            return true;
        }
        else  // slot not in waiting
        {
            return false;
        }


    }

    public boolean pushReserves(T item)
    {

      //  System.out.println("In push reserves exchanger");

        // if slot is not empty , then slot is in use by someone , return null.
        // make slot to waiting and set item and while not timeout wait for matching pop
        // if slot is done , return true
        // if unable to change to empty on timeout, then return true.
        long start = System.currentTimeMillis();
        long end = start+timeout;

        if (slot.compareAndSet(null,item,EMPTY,WAITING))
        {

            while(System.currentTimeMillis() <end)
            {
                if (slot.getStamp()==DONE)
                {

                    slot.compareAndSet(item,null,DONE,EMPTY);
                    return true;
                }
            }

            // time out , check one last time
            if (slot.compareAndSet(item,null,WAITING,EMPTY))
                return false;
            else  // a push thread must have changed state
            {

                boolean result = slot.compareAndSet(item,null,DONE,EMPTY);
                if (!result)
                    System.out.println("Slot is in an erroneous state in push reserves");


                return true;
            }


        }
        else {

            return false;
        }



    }

    public T popMatchesPush()
    {

     //   System.out.println("In pop matches exchanger");

        // if slot is empty , then wait for it to become waiting , if not timeout.
        // if slot is waiting , then get item and make slot done and return with the item.

        // if timeout and empty , then return null.

        T t = slot.getReference();
        if (t==null)
            return t;



        if (slot.compareAndSet(t,null,WAITING,DONE))
        {
            return t;
        }
        else  // slot not in waiting
        {
            return null;
        }

    }
}
