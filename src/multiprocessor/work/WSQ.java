package multiprocessor.work;

import multiprocessor.sets.Node;

import java.util.concurrent.atomic.AtomicReference;

import static multiprocessor.work.WSQNode.Status.*;

public class WSQ {


    AtomicReference<WSQNode> tail = new AtomicReference<>(null);

    public void add(WSQNode node)
    {
        while(true)
        {
            WSQNode curr = tail.get();
            if (tail.compareAndSet(curr,node)) {
                node.pred=curr;
                node.status.set(AVAIALABLE);
                return;
            }


        }
    }

    public WSQNode steal() {
        // steal from tail
        WSQNode curr;
        while (true) {
            curr = tail.get();

            if (curr == null)  //tail is empty , look in other queues.
                return null;

            if (curr.status.get() != AVAIALABLE)  // not not yet correctly in the queue
                continue;

            WSQNode prev = curr.pred;

            if (tail.compareAndSet(curr,prev))
            {
                if (curr.status.compareAndSet(AVAIALABLE,BUSY))
                {
                    return curr;
                }
                else // somebody else working on it .
                {
                    return null;
                }
            }



        }

    }


    public WSQNode remove()
    {

        WSQNode curr;
        while(true) {
             curr = tail.get();

             if (curr==null)  //rtail is empty , look in other queues.
                 return null;

            if (curr.status.get()!=AVAIALABLE)  // not not yet correctly in the queue
                continue;



        while(true) // move to head of queue
        {
            if (curr==null)
                return null;

            WSQNode pred = curr.pred;

            if (pred==null|| pred.status.get()==DONE)
                break;
            else
                curr = curr.pred;
        }

        curr.pred=null; // all done tasks can be removed from the queue.

        if (curr!=null && curr.status.compareAndSet(AVAIALABLE,BUSY))
        {
            return curr;
        }

        // somebody else stole the task , go back to tail .

        }

    }


}
