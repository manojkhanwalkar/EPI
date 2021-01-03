package multiprocessor.stacks;

import java.util.Random;

public class EliminationBackoffStack<T> extends LockFreeStack<T> {


    static final int capacity = 5;
    Exchanger<T>[] popArray =  (Exchanger<T>[]) new Exchanger[capacity];
    Exchanger<T>[] pushArray =  (Exchanger<T>[]) new Exchanger[capacity];


    Random random = new Random();

    public EliminationBackoffStack()
    {
        for (int i=0;i<capacity;i++)
        {
            popArray[i] = new Exchanger<>();
            pushArray[i] = new Exchanger<>();
        }
    }

    private int getSlot()
    {
        return random.nextInt(capacity);
    }

    private boolean pushBackoffHelper(T item)
    {
        int num = getSlot();

        // first check if there is a pop thread to exchange
        Exchanger<T> exchanger = popArray[num];

        if (exchanger.pushMatchesPop(item))
        {
            return true;
        }
        else if (exchanger.pushReserves(item))
        {
            return true;
        }

        return false;
    }

    @Override
    public void push(T t) {

        Node<T> node = new Node(t);

        while(!tryPush(node))
        {
              if (pushBackoffHelper(t))
                  return;
        }

        //System.out.println("Push worked");


    }

    private T popBackoffHelper()
    {
        int num = getSlot();

        // first check if there is a push thread to exchange
        Exchanger<T> exchanger = pushArray[num];
        T t = exchanger.popMatchesPush();
        if (t!=null)
            return t;

        t = exchanger.popReserves();
        if (t!=null)
            return t;

        return null;
    }

    @Override
    public T pop() {

        T t = null;

        while(true) {

            //System.out.println("In pop " + Thread.currentThread().getName());

            t = tryPop();

            if (t!=null)
                return t;


                t= popBackoffHelper();

                if (t!=null)
                    return t;



        }




        }
}
