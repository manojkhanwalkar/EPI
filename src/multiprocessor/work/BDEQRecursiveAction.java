package multiprocessor.work;

import java.util.concurrent.atomic.AtomicInteger;

public class BDEQRecursiveAction extends BDERA {


static AtomicInteger number = new AtomicInteger(100);

    int mynumber;

    public BDEQRecursiveAction()
    {

        super(Thread.currentThread().getName());
 //       System.out.println(this + "  " + number);
//
        this.mynumber = number.getAndDecrement();
    }



    public void run()
    {

        if (mynumber<1)
            return;


        System.out.println("Created by " + created +  " processed by " + Thread.currentThread().getName() +"    " + mynumber);


        BDEQRecursiveAction left = new BDEQRecursiveAction();
            BDEQRecursiveAction right = new BDEQRecursiveAction();

            // no join for now

            left.compute();


            right.compute();

    }

}
