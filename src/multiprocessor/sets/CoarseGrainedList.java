package multiprocessor.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedList<T> extends GrainedList<T> {



    public CoarseGrainedList()
    {
        super();

    }




    Lock lock = new ReentrantLock();


    @Override
    public boolean add(T t) {

        try {
            int key = t.hashCode();
            lock.lock();
            Node1<T> pred = head;
            Node1<T> curr = head;

            while(curr.key<key)
            {
                pred = curr;
                curr = curr.next;
            }

           /* if (curr.key==0 || key==0)
            {
                System.out.println();
            }*/

            if (curr.key==key)
            {
                return false;  // duplicate so unable to add
            }

            Node1 node = new Node1(t);

            node.key = key;

            pred.next = node;
            node.next = curr;

            return true;

        } finally{
            lock.unlock();

        }

    }

    @Override
    public boolean remove(T t) {

        try {
            int key = t.hashCode();
            lock.lock();
            Node1<T> pred = head;
            Node1<T> curr = head;

            while(curr.key<key)
            {
                pred = curr;
                curr = curr.next;
            }

            if (curr.key==key)
            {
                pred.next = curr.next;
                return true;
            }
            else
            {
                return false;
            }




        } finally{
            lock.unlock();

        }

    }

    @Override
    public boolean contains(T t) {

        try {
            int key = t.hashCode();
            lock.lock();

            Node1<T> curr = head;

            while(curr.key<key)
            {

                curr = curr.next;
            }

            if (curr.key==key)
            {
                return true;  // duplicate so unable to add
            }
            else
            {
                return false;
            }




        } finally{
            lock.unlock();

        }
    }
}
