package test.java.r4j;

import org.junit.Test;
import r4j.circuitbreaker.CircuitBreaker;
import r4j.circuitbreaker.CircuitBreakerConfig;
import r4j.ratelimiter.RateLimiter;
import r4j.ratelimiter.RateLimiterConfig;
import r4j.retry.Retry;
import r4j.retry.RetryConfig;
import r4j.run.Decorator;
import r4j.run.Try;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TestDecorator {

    @Test
    public void testDecorator()
    {

        var config2 = CircuitBreakerConfig.custom().numberOfEvents(10).percentageError(20).waitDuration(1).build();
        CircuitBreaker circuitBreaker = new CircuitBreaker(config2);

        var config = RetryConfig.custom().backoff(1).numRetries(10).waitTime(1).build();
        Retry retry = new Retry(config);

        var config1 = RateLimiterConfig.custom()
                .duration(1).permits(5).timeout(5).build();

        RateLimiter limiter = new RateLimiter(config1);



        for (int i=0;i<100;i++) {

            CompletableFuture.runAsync(()->{

            var supplier = Decorator.ofSupplier(TestDecorator::rateLimitProcess)
                    .withRateLimiter(limiter).withCircuitBreaker(circuitBreaker).withRetry(retry).decorate();

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
        Random random = new Random();

        var toss = random.nextInt(10);

        if (toss>0) {
            return "rateLimitProcessTest  " + Thread.currentThread().getName() + "  " + count.getAndIncrement();
        }
        else
        {
            throw new RuntimeException("Simulating error ");
        }

        //

        //System.out.println("Hello from rate limit");
    }


}
