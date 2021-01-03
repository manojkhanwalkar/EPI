package java891011121314.counters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CounterTester {

    public static void main(String[] args) {

        CounterTester counterTester = new CounterTester();
        counterTester.test();

    }


    public void test() {

        CombiningTree combiningTree = new CombiningTree(20);

        ExecutorService service = Executors.newFixedThreadPool(10);


        for (int i=0;i<20;i++)
        {
            service.submit(new CounterTask(i%4,null,combiningTree));
        }



    }

    static class CounterTask implements Runnable
    {
        int leaf ;
        Consumer consumer ;
        CombiningTree combiningTree;
        public CounterTask(int leaf, Consumer<Integer> consumer, CombiningTree combiningTree)
        {
            this.leaf = leaf;
            this.consumer = consumer;
            this.combiningTree = combiningTree;
        }

        public void run()
        {
            int count=0;
            for (int i=0;i<10;i++)
            {
               count = combiningTree.getAndIncrement(leaf);
            }
                System.out.println(count + "  " + Thread.currentThread().getName());
        }


    }


}
