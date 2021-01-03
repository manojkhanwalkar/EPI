package multiprocessor.sets;

public class FineGrainedList<T> extends GrainedList<T> {



    public FineGrainedList()
    {
        super();

    }




    //Lock lock = new ReentrantLock();


    @Override
    public boolean add(T t) {

        int key = t.hashCode();
        Node1<T> pred = head;
        Node1<T> curr = head.next;


        try {


            pred.lock();
            curr.lock();

            while(curr.key<key)
            {
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }



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

            pred.lock(); curr.lock();
            while(curr.key<key)
            {
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
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
            pred.unlock();
            curr.unlock();

        }

    }

    @Override
    public boolean contains(T t) {

        int key = t.hashCode();


        Node1<T> curr = head;


        try {

            curr.lock();

            while(curr.key<key)
            {
                curr.next.lock();
                curr.unlock();
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
            curr.unlock();

        }
    }
}
