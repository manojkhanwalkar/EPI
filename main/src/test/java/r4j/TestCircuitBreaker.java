package test.java.r4j;


import org.junit.Test;
import r4j.circuitbreaker.CircuitBreaker;
import r4j.circuitbreaker.CircuitBreakerConfig;
import r4j.run.Try;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TestCircuitBreaker {

    @Test
    public void testCircuitBreaker()
    {
        var config = CircuitBreakerConfig.custom().numberOfEvents(10).percentageError(20).waitDuration(1).build();
        CircuitBreaker circuitBreaker = new CircuitBreaker(config);


        for (int i=0;i<100;i++) {

            CompletableFuture.runAsync(()->{

            var supplier = CircuitBreaker.decorateSupplier(circuitBreaker,TestCircuitBreaker::process);
            var result = Try.ofSupplier(supplier).get();

            System.out.println(result);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static        AtomicInteger count = new AtomicInteger(0);

    private static String process()
    {

       // System.out.println("Process called");
        Random random = new Random();

        var toss = random.nextInt(10);

       if (toss>0) {
            return "CircuitBreakerTest  " + Thread.currentThread().getName() + "  " + count.getAndIncrement();
        }
        else
        {
            System.out.println("Process error");
            throw new RuntimeException("Simulating error ");
        }

        //
      //  return "CircuitBreakerTest  " + Thread.currentThread().getName() + "  " + count.getAndIncrement();


        //System.out.println("Hello from rate limit");
    }


}
