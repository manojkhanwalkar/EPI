package test.java.r4j;

import org.junit.Test;
import r4j.bulkhead.Bulkhead;
import r4j.bulkhead.BulkheadConfig;
import r4j.ratelimiter.RateLimiterConfig;
import r4j.run.Try;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestBulkhead {

    @Test
    public void testBulkheadConfig()
    {
        var config = RateLimiterConfig.custom()
                .duration(1).permits(1).timeout(5).build();



    }

    @Test
    public void testBulkhead()
    {
        var config = BulkheadConfig.custom().concurrent(1)
                .timeout(5).build();

        Bulkhead bulkhead = new Bulkhead(config);

        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i=0;i<100;i++) {

            CompletableFuture.runAsync(()->{

            var supplier = Bulkhead.decorateSupplier(bulkhead, TestBulkhead::rateLimitProcess);

            var result = Try.ofSupplier(supplier).get();

            System.out.println(result);

            },service);
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
