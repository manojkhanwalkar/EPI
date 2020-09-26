package multiprocessor.stacks;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> implements S<T> {

    volatile AtomicReference<Node<T>> top = new AtomicReference<>(null);

    static class Node<T>
    {

        T value;
        Node<T> next;

        public Node(T t)
        {
            value = t;
            next = null;
        }

    }

    @Override
    public T pop() {

        while(true) {

            //System.out.println("In pop " + Thread.currentThread().getName());

            T t = tryPop();
            if (t!=null)
              return t;
            else
            {
      /*          try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }

        }

    }

     T tryPop()
    {
        Node<T> old = top.get();

     //   System.out.println("In try pop " + Thread.currentThread().getName());

        if (old==null)
            return null;

        if (top.compareAndSet(old,old.next))
        {
      //      System.out.println("Pop worked");
            return old.value;
        }
        else
        {
           // System.out.println("Pop failed") ;
            return null;
        }


    }

     boolean tryPush(Node<T> node)
    {
        Node<T> old = top.get();
        node.next = old;

        return top.compareAndSet(old,node);


    }

    @Override
    public void push(T t) {

        Node<T> node = new Node(t);

        while(!tryPush(node))
        {
          //  System.out.println("Push failed");
         /*   try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

        //System.out.println("Push worked");


    }
}
