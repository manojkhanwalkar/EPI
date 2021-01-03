package multiprocessor.work;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BDERA implements Runnable{

    public static ThreadLocal<BDEQProcessor> processor = new ThreadLocal<>();

    String created;


    public BDERA(String name)
    {
        this.created = name;
    }


    public final void fork()
    {
        BDEQProcessor wsqProcessor = processor.get();

        wsqProcessor.submit(this);
    }

    public final void join()
    {


    }


 //   volatile boolean complete = false;

    AtomicBoolean complete = new AtomicBoolean(false);

    public final void compute()
    {
      //  System.out.println("CFSTask " + this  + "  started");
        run();
        complete.set(true);

     //   System.out.println("CFSTask " + this  + "  completed");
    }

    public abstract void run();



}
