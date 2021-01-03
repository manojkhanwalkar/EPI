package multiprocessor.stacks;


import com.google.common.util.concurrent.Runnables;
import multiprocessor.pool.Pool;
import multiprocessor.pool.PoolTester;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class StackTester {


    public static void main(String[] args) {

        StackTester setTester = new StackTester();
        setTester.testCoarseGrainedLock();
    }

    ExecutorService service = Executors.newFixedThreadPool(50);

    ExecutorService service1= Executors.newFixedThreadPool(50);



    public  void testCoarseGrainedLock() {


        S<String> set = new EliminationBackoffStack<>();

        int start = 0;


        for (int i=0;i<5;i++)
        {
            //   service.submit(new Inserter(start,end,pool));
            service1.submit(new Dequeuer(set));
            //    start=end;
            //    end+=100;
        }

        for (int i=0;i<500;i++)
        {
            service.submit(new Inserter(start,++start,set));
            //service.submit(new Dequeuer(pool));


        }




/*
      try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/








    }


    static class Inserter implements Runnable {
        int start;
        int end;
        S<String> set;

        public Inserter(int start, int end,  S<String> set) {
            this.start = start;
            this.end = end;
            this.set = set;
        }


        public void run() {
            String s = "Enqueued by " + Thread.currentThread().getName() + " ";

            for (int i = start; i < end; i++) {
                set.push(s+i);

             /*   try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }

        }

    }


    static class Dequeuer implements Runnable
    {
        S<String> set;
        public Dequeuer( S<String> set)
        {

            this.set =set;
        }


        public void run()
        {
            String s = "Dequeued by " + Thread.currentThread().getName();

            while(true)
            {
              //  System.out.println("In run "  + Thread.currentThread().getName()) ;

                String res = set.pop();
                if (res!=null)
                {
                    System.out.println(res + " " + s);
                }
                else
                {


                  /*  try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }


        }

    }



}
