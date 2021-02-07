package resilience;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.internal.SemaphoreBasedRateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

import javax.cache.Caching;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class ResilienceTester {

    public static void main(String[] args) throws Exception {

       // testCircuitBreaker();

       // testRateLimiter();

       // Thread.sleep(100000*1000);

       // testCache();

       // testAllFeatures();

        testTimeLimiter();

    }

    public static void testTimeLimiter() throws Exception
    {

// Create a TimeLimiter
        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));
// The Scheduler is needed to schedule a timeout on a non-blocking CompletableFuture
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

// The non-blocking variant with a CompletableFuture
        CompletableFuture<String> result = timeLimiter.executeCompletionStage(
                scheduler, () -> CompletableFuture.supplyAsync(()-> "hello world")).toCompletableFuture();

        System.out.println(result.get());

// The blocking variant which is basically future.get(timeoutDuration, MILLISECONDS)
        String result1 = timeLimiter.executeFutureSupplier(
                () -> CompletableFuture.supplyAsync(() -> "hello world"));

        System.out.println(result1);


    }

    public static void testAllFeatures() throws ExecutionException, InterruptedException {
        // Create a CircuitBreaker with default configuration
        CircuitBreaker circuitBreaker = CircuitBreaker
                .ofDefaults("backendService");

// Create a Retry with default configuration
// 3 retry attempts and a fixed time interval between retries of 500ms
        Retry retry = Retry
                .ofDefaults("backendService");

// Create a Bulkhead with default configuration
        Bulkhead bulkhead = Bulkhead
                .ofDefaults("backendService");

        Supplier<String> supplier = ResilienceTester::process;


// Decorate your call to backendService.doSomething()
// with a Bulkhead, CircuitBreaker and Retry
// **note: you will need the resilience4j-all dependency for this
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .withRetry(retry)
                .decorate();

// Execute the decorated supplier and recover from any exception
        String result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> "Hello from Recovery").get();

        System.out.println(result);

// You can also run the supplier asynchronously in a ThreadPoolBulkhead
        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead
                .ofDefaults("backendService");

// The Scheduler is needed to schedule a timeout
// on a non-blocking CompletableFuture
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

        CompletableFuture<String> future = Decorators.ofSupplier(supplier)
                .withThreadPoolBulkhead(threadPoolBulkhead)
                .withTimeLimiter(timeLimiter, scheduledExecutorService)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(asList(Exception.class,
                        CallNotPermittedException.class,
                        BulkheadFullException.class),
                        throwable -> "Hello from Recovery")
                .get().toCompletableFuture();

        System.out.println(future.get());
    }

    public static void testCache()
    {
        // Create a CacheContext by wrapping a JCache instance.
        javax.cache.Cache<String, String> cacheInstance = Caching
                .getCache("cacheName", String.class, String.class);
        Cache<String, String> cacheContext = Cache.of(cacheInstance);


        CheckedFunction1<String, String> cachedFunction = Decorators
                .ofCheckedSupplier(() -> process())
                .withCache(cacheContext)
                .decorate();

        cacheContext.getEventPublisher()
                .onCacheHit(event -> System.out.println(event))
    .onCacheMiss(event -> System.out.println(event))
    .onError(event -> System.out.println(event));
        String value = Try.of(() -> cachedFunction.apply("cacheKey")).get();

        System.out.println(value);
    }

    public static void testRateLimiter()
    {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofSeconds(3))
                .build();

      //  SemaphoreBasedRateLimiter

// Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = rateLimiterRegistry
                .rateLimiter("name2", config);

        RateLimiter semRateLimiter = new SemaphoreBasedRateLimiter("sem",config);

        // Decorate your call to BackendService.doSomething()
        CheckedRunnable restrictedCall = RateLimiter
                .decorateCheckedRunnable(rateLimiter,ResilienceTester::rateLimitProcess);


        for (int i=0;i<100;i++) {
            CompletableFuture.runAsync(()->{

            Try.run(restrictedCall)
                    .andThenTry(restrictedCall)
                    .onFailure(throwable -> {
                       // System.out.println("Rate limit exceeded");
                    });
            //.onFailure((RequestNotPermitted throwable) -> System.out.println("Wait before call it again :)"));
            });

        }

        rateLimiter.getEventPublisher()
                .onSuccess(event -> System.out.println(event))
        .onFailure(event -> System.out.println(event));

    }

    public static void rateLimitProcess()
    {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("Hello from rate limit");
    }

    public static void testCircuitBreaker()
    {
// Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(2)
                .recordExceptions(IOException.class, TimeoutException.class)
              //  .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

// Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        CircuitBreaker circuitBreaker = circuitBreakerRegistry
                .circuitBreaker("name");

        Supplier<String> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, ResilienceTester::process);

        String result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> "Hello from Recovery").get();

        System.out.println(result);

    }

     static String process()
    {
        throw new RuntimeException("exception");
        //return "Hello World";
    }
}
