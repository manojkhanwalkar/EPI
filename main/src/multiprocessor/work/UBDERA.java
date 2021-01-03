package multiprocessor.work;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UBDERA implements Runnable{

    public static ThreadLocal<UBDEQProcessor> processor = new ThreadLocal<>();

    String created;


    public UBDERA(String name)
    {
        this.created = name;
    }


    public final void fork()
    {
        UBDEQProcessor wsqProcessor = processor.get();

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
