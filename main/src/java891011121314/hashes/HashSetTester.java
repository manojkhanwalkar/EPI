package java891011121314.hashes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashSetTester {


    public static void main(String[] args) throws Exception {


      //  CGHashSet<Integer> test = new CGHashSet<>(2);

        RefinableHashSet<Integer> test = new RefinableHashSet<>(2,16);

        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i=0;i<100;i++)
        {
            int x = i;
            service.submit(()->test.put(x));
        }

        Thread.sleep(1000);


        test.print();



    }
}
