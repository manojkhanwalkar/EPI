package java891011121314.flow;

import java.util.Random;

public class FlowTester {


    public static void main(String[] args) {

        Random random = new Random();
        Cache cache = Cache.getInstance();

        Book book = new Book();

        ConsoleWriter orderWriter = new ConsoleWriter();

        ConsoleWriter execWriter = new ConsoleWriter();


        cache.subscribe(orderWriter);
        cache.subscribe(book);

        book.subscribe(execWriter);

        cache.start();
        for (int i=0;i<10;i++)
        {
            Order order = new Order("IBM","Order"+i, 10+i*i , random.nextInt());

          //  Execution execution = new Execution("Exec"+i, "Order+i",order.qty,order.price);

            cache.order(order);
        //    cache.execution(execution);
        }




        cache.stop();

    }
}
