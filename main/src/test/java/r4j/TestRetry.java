package test.java.r4j;

import org.junit.Test;
import r4j.retry.Retry;
import r4j.retry.RetryConfig;
import r4j.run.Try;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRetry {

    @Test
    public void testRetryConfig()
    {
        var config = RetryConfig.custom().backoff(1).numRetries(2).waitTime(1).build();
    }

    @Test
    public void testRateLimiter()
    {
        var config = RetryConfig.custom().backoff(1).numRetries(10).waitTime(1).build();

        Retry retry = new Retry(config);

        for (int i=0;i<1;i++) {

            CompletableFuture.runAsync(()->{

            var supplier = Retry.decorateSupplier(retry, TestRetry::rateLimitProcess);

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

        //return "rateLimitProcessTest  " + Thread.currentThread().getName()  + "  " + count.getAndIncrement();

        throw new RuntimeException("Simulating error ");

        //System.out.println("Hello from rate limit");
    }


}
