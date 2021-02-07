package test.java.r4j;

import org.junit.Test;
import r4j.timelimiter.TimeLimiter;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TestTimeLimiter {

    @Test
    public void testFutureTimeout()
    {
        var future = CompletableFuture.supplyAsync(()->{

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Hello ";
        });

        try {
            future.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (ExecutionException e) {
           // e.printStackTrace();
        } catch (TimeoutException timeoutException) {
            timeoutException.printStackTrace();
        }
    }

    @Test
    public void testTimeLimiter1()
    {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        TimeLimiter limiter =  TimeLimiter.of(Duration.ofSeconds(1));

        for (int i=0;i<100;i++) {
            try {
                var result = limiter.executeCompletionStage(scheduler,()-> CompletableFuture.supplyAsync(TestTimeLimiter::rateLimitProcess));

                System.out.println(result.toCompletableFuture().get());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTimeLimiter()
    {

        TimeLimiter limiter =  TimeLimiter.of(Duration.ofSeconds(1));

        for (int i=0;i<100;i++) {
           try {
                    var result = limiter.executeFutureSupplier(()-> CompletableFuture.supplyAsync(TestTimeLimiter::rateLimitProcess));

                    System.out.println(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
     }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static        AtomicInteger count = new AtomicInteger(0);

    private static String rateLimitProcess()
    {

        System.out.println("In time limit process " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
           System.out.println("Interrupted");
        }

        return "TimeLimitProcessStep  " + Thread.currentThread().getName()  + "  " + count.getAndIncrement();
        //System.out.println("Hello from rate limit");
    }


}
