package multiprocessor.sets;

public class OptimisticGrainedList<T> extends GrainedList<T> {



    public OptimisticGrainedList()
    {
        super();

    }




    //Lock lock = new ReentrantLock();

    private boolean validate(Node1<T> prev , Node1<T> curr)
    {
        Node1<T> node = head;
        while(node.key<prev.key) {
            node = node.next;
        }

        return (node==prev && prev.next==curr);
    }

  //  Set<Integer> debug = new HashSet<>();

    @Override
    public boolean add(T t) {

        int key = t.hashCode();
        Node1<T> pred = head;
        Node1<T> curr = head.next;


        try {
            while(curr.key<key)
            {

                pred = curr;
                curr = curr.next;

            }

            pred.lock(); curr.lock();



            if (curr.key==key || !validate(pred,curr))
            {
                return false;  // duplicate so unable to add
            }



        /*    if (debug.contains(key))
            {
                System.out.println("Problem " + key);
            }
            else
            {
                debug.add(key);
            }*/

            Node1 node = new Node1(t);

            node.key = key;

            pred.next = node;
            node.next = curr;

            return true;

        } finally{
            pred.unlock();
            curr.unlock();


        }

    }

    @Override
    public boolean remove(T t) {

        int key = t.hashCode();

        Node1<T> pred = head;
        Node1<T> curr = head;


        try {


            while(curr.key<key)
            {

                pred = curr;
                curr = curr.next;

            }

            pred.lock(); curr.lock();

            if (curr.key==key && validate(pred,curr))
            {
                pred.next = curr.next;
               // System.out.println("Key removed is " + key);
                return true;
            }
            else
            {
                return false;
            }




        } finally{
            pred.unlock();
            curr.unlock();

        }

    }

    @Override
    public boolean contains(T t) {

        int key = t.hashCode();


        Node1<T> prev = head;
        Node1<T> curr = head.next;

        try {

            while(curr.key<key)
            {
                prev = curr;
                curr = curr.next;

            }

            prev.lock(); curr.lock();

            if (curr.key==key && validate(prev,curr))
            {
                return true;  // duplicate so unable to add
            }
            else
            {
                return false;
            }




        } finally{
            curr.unlock();
            prev.unlock();

        }
    }
}
