package multiprocessor.pool;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WFUBQ<T> implements Pool<T> {

    static class Node<T>
    {
       T value;

       AtomicReference<Node<T>> next;

       public Node(T value)
       {
           this.value = value;
           next = new AtomicReference<>(null);
       }
    }

    AtomicReference<Node<T>> head ;
    AtomicReference<Node<T>> tail;


    public WFUBQ()
    {
        Node<T> dummy = new Node<>(null);
        head = new AtomicReference<>(dummy);  // sentinel node
        tail = new AtomicReference<>(dummy);
    }

    @Override
    public T get() {

         while(true)
         {
             Node<T> first = head.get();
             Node<T> last = tail.get();

             Node<T> next = first.next.get();

             if (first==head.get())
             {
                 if (first==last) {
                     if (next == null) {

                         // empty queue -
                         return null;

                     }

                     // first is same as last , but next is not null so tail pointer is incorrect and needs fixing
                     tail.compareAndSet(last,next);
                 }
                 else
                 {
                     T result = next.value;
                     if (head.compareAndSet(first,next))
                         return result;
                 }
             }
         }

    }

    @Override
    public void put(T value) {
        Node<T> node = new Node<>(value);

        while(true)
        {
            Node<T> last = tail.get();
            Node<T> next = last.next.get();
            if (last==tail.get())
            {
                if (next==null)
                {
                    if (last.next.compareAndSet(next,node))
                    {
                        tail.compareAndSet(last,node);
                        return ;
                    }
                }
                else
                {
                    tail.compareAndSet(last,next);
                }
            }


        }

    }
}
