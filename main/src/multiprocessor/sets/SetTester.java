package multiprocessor.sets;

import com.google.common.util.concurrent.Runnables;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class SetTester {


    public static void main(String[] args) {

        SetTester setTester = new SetTester();
        setTester.testCoarseGrainedLock();
    }

    ExecutorService service = Executors.newCachedThreadPool();

    Random random = new Random();


    public  void testCoarseGrainedLock()
    {


     //   WFSet<Integer> list = new CoarseGrainedList<>();
    //    WFSet<Integer> list = new FineGrainedList<>();
     //   WFSet<Integer> list = new OptimisticGrainedList<>();

     //   WFSet<Integer> list = new LazyOptimisticGrainedList<>();

        WFSet<Integer> list = new LockfreeGrainedList<>();




        {

            Consumer<WFSet<Integer>> adder = (list1) -> {
                for (int i=0;i<10000;i++)
                {
                    list1.add(random.nextInt(100));
                }

            };

            List<Runnable> adders = create(Do.class, list, adder, 5);

            List<Future> futures = create(adders);

            waitAndPrint(futures, list);
        }

        {
            Consumer<WFSet<Integer>> remover = (list1) -> {
                for (int i=0;i<10;i++)
                {
                    int num =  random.nextInt(100);

                    System.out.println( num + " removed from list  " + list.remove(num));

                }

            };

            List<Runnable> removers = create(Do.class,list,remover,5);

            List<Future> futures = create(removers);

            waitAndPrint(futures,list);
        }


        {

            Consumer<WFSet<Integer>> container = (list1) -> {
                for (int i=0;i<5;i++)
                {
                    int num =  random.nextInt(100);
                    System.out.println( num + " list contains " + list.contains(num));
                }
            };
            List<Runnable> containers = create(Do.class,list,container,5);

            List<Future> futures = create(containers);

            waitAndPrint(futures,list);
        }


      /*  {

            List<Runnable> adders = create(Adder.class, list);

            List<Future> futures = create(adders);

            waitAndPrint(futures, list);
        }



        {
            List<Runnable> containers = create(Container.class,list);

            List<Future> futures = create(containers);

            waitAndPrint(futures,list);
        }


    */



    }

    //final int NUM = 5;
    private List<Runnable> create(Class<?> claz, WFSet<Integer> list, int NUM)
    {
        List<Runnable> runnables = new ArrayList<>();


        for (int i=0;i<NUM;i++) {

            try {
                Runnable r= (Runnable) claz.getConstructor(WFSet.class).newInstance(list);
                runnables.add(r);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }


        }


        return runnables;

    }

    private List<Runnable> create(Class<?> claz, WFSet<Integer> list, Consumer consumer, int NUM)
    {
        List<Runnable> runnables = new ArrayList<>();


        for (int i=0;i<NUM;i++) {

            try {
                Runnable r= (Runnable) claz.getConstructor(WFSet.class, Consumer.class).newInstance(list,consumer);
                runnables.add(r);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }


        }


        return runnables;

    }

    private List<Future> create(List<Runnable> runnables)
    {

        List<Future> futures = new ArrayList<>();

        for (int i=0;i<runnables.size();i++)
        {
            Future f = service.submit(runnables.get(i));

            futures.add(f);
        }

        return futures;
    }

    private void waitAndPrint(List<Future> futures,WFSet<Integer> list )
    {
        futures.stream().forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        list.print();

    }


    static class Do implements Runnable
    {
        WFSet<Integer> list ;

        Consumer<WFSet<Integer>> consumer;


        public Do(WFSet<Integer> list, Consumer<WFSet<Integer>> consumer)
        {
            this.list = list;
            this.consumer = consumer;
        }


        public void run()
        {
            consumer.accept(list);


        }
    }


  /*  static class Adder implements Runnable
    {

        CoarseGrainedList<Integer> list ;
        public Adder(CoarseGrainedList<Integer> list)
        {
            this.list = list;
        }

        Random random = new Random();

        public void run()
        {
            for (int i=0;i<10000;i++)
            {
                list.add(random.nextInt(100));
            }

        }
    }*/

/*
    static class Remover implements Runnable
    {

        CoarseGrainedList<Integer> list ;
        public Remover(CoarseGrainedList<Integer> list)
        {
            this.list = list;
        }

        Random random = new Random();

        public void run()
        {
            for (int i=0;i<5;i++)
            {
                int num =  random.nextInt(100);

                System.out.println( num + " removed from list  " + list.remove(num));

            }

        }
    }*/


  /*  static class Container implements Runnable
    {

        CoarseGrainedList<Integer> list ;
        public Container(CoarseGrainedList<Integer> list)
        {
            this.list = list;
        }

        Random random = new Random();

        public void run()
        {
            for (int i=0;i<5;i++)
            {
               int num =  random.nextInt(100);
               System.out.println( num + " list contains " + list.contains(num));
            }

        }
    }*/
}
