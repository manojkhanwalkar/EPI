package multiprocessor.work;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WSQRA implements Runnable{

    public static ThreadLocal<WSQProcessor> processor = new ThreadLocal<>();


    public WSQRA()
    {

    }


    public final void fork()
    {
        WSQProcessor wsqProcessor = processor.get();

        wsqProcessor.submit(this);
    }

    public final void join()
    {
        //while(!complete.get());

        try {
            Thread.sleep(1);

            System.out.println(this + "  " + complete.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


 //   volatile boolean complete = false;

    AtomicBoolean complete = new AtomicBoolean(false);

    public final void compute()
    {
        System.out.println("CFSTask " + this  + "  started");
        run();
        complete.set(true);

        System.out.println("CFSTask " + this  + "  completed");
    }

    public abstract void run();

    WSQProcessor wsqProcessor;


}
