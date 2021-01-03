package multiprocessor.work;

import java.util.concurrent.atomic.AtomicInteger;

public class UBDEQRecursiveAction extends UBDERA {


static AtomicInteger number = new AtomicInteger(10);

    int mynumber;

    public UBDEQRecursiveAction()
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



        UBDEQRecursiveAction left = new UBDEQRecursiveAction();
            UBDEQRecursiveAction right = new UBDEQRecursiveAction();

            // no join for now

            left.fork();


            right.fork();

    }

}
