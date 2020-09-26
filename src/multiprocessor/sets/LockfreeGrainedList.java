package multiprocessor.sets;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockfreeGrainedList<T>  implements WFSet<T> {



    Node<T> head = new Node();
    Node<T> tail = new Node();


    public LockfreeGrainedList()
    {
        head.next = new AtomicMarkableReference<>(tail,false);
        tail.next = new AtomicMarkableReference<>(null,false);
        head.key = Integer.MIN_VALUE;
        tail.key=Integer.MAX_VALUE;
    }

    static class Window
    {
        Node pred, curr;

        public Window(Node pred, Node curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }




    public Window find(Node head, int key)
    {
        Node<T> pred = null, curr = null , succ = null;
        boolean[] marked = {false};

        boolean snip;

        retry: while(true)
        {
            pred = head;
            curr = pred.next.getReference();
            while(true)
            {
                succ = curr.next.get(marked);
                while(marked[0])
                {
                    snip = pred.next.compareAndSet(curr,succ,false,false);
                    if (!snip)
                        continue retry;
                    curr = succ;
                    succ = curr.next.get(marked);
                }

                if (curr.key>=key)
                {
                    return new Window(pred,curr);
                }

                pred = curr;
                curr = succ;
            }

        }




    }




    public boolean add(T t) {

        int key = t.hashCode();

        while(true)
        {
            Window window = find(head,key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;
            if (curr.key==key)
            {
                return false;
            }
            else
            {
                Node<T> node = new Node<>(t);
                node.key = key;
                node.next = new AtomicMarkableReference<>(curr,false);
                if (pred.next.compareAndSet(curr,node,false,false))
                    return true;
            }

        }




    }


    public boolean remove(T t) {

        int key = t.hashCode();

        while(true) {

            Window window = find(head, key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;

            if (curr.key != key) {

                return false;
            }

            Node succ = curr.next.getReference();

            // mark it for removal
            boolean snip = curr.next.compareAndSet(succ, succ, false, true);
            if (!snip)
                continue;

            pred.next.compareAndSet(succ,succ,false,false);

            return true;


        }



    }


    public boolean contains(T t) {

        int key = t.hashCode();


        Node<T> curr = head;


            while(curr.key<key)
            {
                
                curr = curr.next.getReference();


            }



            return (curr.key==key &&  curr.next.isMarked());


    }

    @Override
    public void print()
    {


        Node<T> curr = head;

        int count=0;

        while(curr!=null)
        {
            count++;

            System.out.print(curr.key) ;
            System.out.print(" ");

            curr = curr.next.getReference();
        }

        System.out.println();

        System.out.println(count);



        // set.stream().sorted().forEach(System.out::println);
    }
}
