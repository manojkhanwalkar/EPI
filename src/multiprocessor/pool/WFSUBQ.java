package multiprocessor.pool;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class WFSUBQ<T> implements Pool<T> {

    static class Node<T>
    {
       T value;

       AtomicStampedReference<Node<T>> next;

       public Node(T value)
       {
           this.value = value;
           next = new AtomicStampedReference<>(null,0);
       }
    }

    AtomicStampedReference<Node<T>> head ;
    AtomicStampedReference<Node<T>> tail;

    ThreadLocal<Queue<Node<T>>>  localNodes = ThreadLocal.withInitial(ArrayDeque::new );


    private Node<T> getNode(T value)
    {
        Queue<Node<T>>  queue = localNodes.get();

        if (queue.isEmpty())
        {
            return new Node<>(value);
        }
        else
        {
            return queue.remove();
        }
    }


    private void returnNode(Node<T> node)
    {

        Queue<Node<T>>  queue = localNodes.get();
    }


    public WFSUBQ()
    {
        Node<T> dummy = getNode(null);
        head = new AtomicStampedReference<>(dummy,0);  // sentinel node
        tail = new AtomicStampedReference<>(dummy,0);
    }

    @Override
    public T get() {

       // int[] dummy = new int[1];

        int[] f = new int[1];
        int[] n = new int[1];
        int[] l = new int[1];
         while(true)
         {
             Node<T> first = head.get(f);
             Node<T> last = tail.get(l);

             Node<T> next = first.next.get(n);

             if (first==head.get(f))
             {
                 if (first==last) {
                     if (next == null) {

                         // empty queue -
                         return null;

                     }

                     // first is same as last , but next is not null so tail pointer is incorrect and needs fixing
                     tail.compareAndSet(last,next,l[0],l[0]+1);
                 }
                 else
                 {
                     T result = next.value;
                     if (head.compareAndSet(first,next,f[0],f[0]+1)) {

                         returnNode(first);
                         return result;
                     }
                 }
             }
         }

    }

    @Override
    public void put(T value) {

        int[] f = new int[1];
        int[] n = new int[1];
        int[] l = new int[1];

        Node<T> node = getNode(value); // new Node<>(value);

        while(true)
        {
            Node<T> last = tail.get(l);
            Node<T> next = last.next.get(n);
            if (last==tail.get(l))
            {
                if (next==null)
                {
                    if (last.next.compareAndSet(next,node,n[0],n[0]+1))
                    {
                        tail.compareAndSet(last,node,l[0],l[0]+1);
                        return ;
                    }
                }
                else
                {
                    tail.compareAndSet(last,next,l[0],l[0]+1);
                }
            }


        }

    }
}
