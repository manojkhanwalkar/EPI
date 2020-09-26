package multiprocessor.sets;

public class LazyOptimisticGrainedList<T> extends GrainedList<T> {



    public LazyOptimisticGrainedList()
    {
        super();

    }




    //Lock lock = new ReentrantLock();

    private boolean validate(Node1<T> prev , Node1<T> curr)
    {

        return (!prev.marked && !curr.marked && prev.next==curr);

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


            Node1 node = new Node1(t);

            node.marked=false;

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
                curr.marked=true;
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



        Node1<T> curr = head.next;

        try {

            while(curr.key<key)
            {
                
                curr = curr.next;

            }


            if (curr.key==key && !curr.marked)
            {
                return true;
            }
            else
            {
                return false;
            }




        } finally{


        }
    }
}
