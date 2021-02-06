package test.java.r4j;

import org.junit.Test;
import r4j.ratelimiter.RateLimiter;
import r4j.ratelimiter.RateLimiterConfig;
import r4j.run.Try;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRateLimiter {

    @Test
    public void testRateLimiterConfig()
    {
        var config = RateLimiterConfig.custom()
                .duration(1).permits(1).timeout(5).build();



    }

    @Test
    public void testRateLimiter()
    {
        var config = RateLimiterConfig.custom()
                .duration(1).permits(5).timeout(5).build();

        RateLimiter limiter = new RateLimiter(config);

        for (int i=0;i<100;i++) {

            CompletableFuture.runAsync(()->{

            var supplier = RateLimiter.decorateSupplier(limiter, TestRateLimiter::rateLimitProcess);

            var result = Try.ofSupplier(supplier).get();

            System.out.println(result);

            });
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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "rateLimitProcessTest  " + Thread.currentThread().getName()  + "  " + count.getAndIncrement();
        //System.out.println("Hello from rate limit");
    }


}
