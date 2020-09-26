package multiprocessor.pool;

import java.util.concurrent.atomic.AtomicReference;

public class SyncDualQ<T> implements Pool<T> {

    enum NodeType { RESERVATION, ITEM};

    private class Node<T>
    {

        volatile NodeType type ;
        volatile AtomicReference<T> item;
        volatile AtomicReference<Node<T>> next;

        public Node(NodeType type, T t )
        {
            this.type = type;
            item = new AtomicReference<>(t);
            next = new AtomicReference<>(null);
        }

    }

    volatile AtomicReference<Node<T>> head;
    volatile AtomicReference<Node<T>> tail;

    public SyncDualQ()
    {
        Node<T> dummy = new Node(NodeType.ITEM, null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }



    @Override
    public T get() {

        Node<T> offer = new Node<>(NodeType.RESERVATION,null);
        while(true)
        {
            Node<T> t = tail.get();
            Node<T> h = head.get();

            // if queue is empty or of type Reservation
            if (h==t || t.type==NodeType.RESERVATION)
            {
                Node<T> n = t.next.get();
                if (t==tail.get())
                {

                    if (n!=null) // tail needs to be moved
                    {
                        tail.compareAndSet(t,n);
                    }
                    else if (t.next.compareAndSet(n,offer))
                    {
                        tail.compareAndSet(t,offer);
                        while(offer.item.get()==null) ; // spin till some one takes it .
                        h = head.get();
                        if (offer==h.next.get())
                            head.compareAndSet(h,offer);

                        T result = offer.item.get();
                        offer.item.set(null);
                        return result;

                    }
                }
            } else
            {
                Node<T> n = h.next.get();
                T result = n.item.get();
                if (t!=tail.get() || h!= head.get() || n==null ) {
                    continue;
                }
                boolean success = n.item.compareAndSet(result,null);
                head.compareAndSet(h,n);
                if (success)
                    return result;
            }

        }

    }

    @Override
    public void put(T element) {

        Node<T> offer = new Node<>(NodeType.ITEM,element);
        while(true)
        {
            Node<T> t = tail.get();
            Node<T> h = head.get();

            // if queue is empty or of type item
            if (h==t || t.type==NodeType.ITEM)
            {
                Node<T> n = t.next.get();
                if (t==tail.get())
                {

                        if (n!=null) // tail needs to be moved
                        {
                            tail.compareAndSet(t,n);
                        }
                        else if (t.next.compareAndSet(n,offer))
                        {
                            tail.compareAndSet(t,offer);
                            while(offer.item.get()!=null) ; // spin till some one takes it .
                            h = head.get();
                            if (offer==h.next.get())
                                head.compareAndSet(h,offer);

                            return;

                        }
                }
            } else
            {
                Node<T> n = h.next.get();
                if (t!=tail.get() || h!= head.get() || n==null ) {
                    continue;
                }
                boolean success = n.item.compareAndSet(null,element);
                head.compareAndSet(h,n);
                if (success)
                    return;
            }

        }


    }
}
