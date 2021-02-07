package r4j.bulkhead;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Bulkhead {

    int concurrent;
    int timeout;

    Semaphore semaphore ;

   // ScheduledExecutorService scheduler ;

    public Bulkhead(BulkheadConfig config)
    {
        this.concurrent = config.getConcurrent();
        this.timeout = config.getTimeout();

        semaphore = new Semaphore(concurrent,true);

       // scheduler = new ScheduledThreadPoolExecutor(1);
       // scheduler.scheduleAtFixedRate(this::refresh,duration,duration, TimeUnit.SECONDS);
    }



  //  @Override
    public boolean acquirePermission() {
        try {
            boolean success = semaphore
                    .tryAcquire(timeout,
                            TimeUnit.SECONDS);
          //  publishRateLimiterAcquisitionEvent(success, permits);
            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
           // publishRateLimiterAcquisitionEvent(false, permits);
            return false;
        }
    }

    public void release() {

            semaphore.release();

    }



    public static <T> Supplier<T> decorateSupplier(Bulkhead rateLimiter , Supplier<T> supplier)
    {

          return () -> {
            boolean permission = rateLimiter.acquirePermission();
            if (permission) {
                try {
                    return supplier.get();
                    //  rateLimiter.onSuccess();
                } catch (Exception exception) {
                    //rateLimiter.onError(exception);
                    throw exception;
                } finally {
                    rateLimiter.release();
                }
            }
            else
                {
                    System.out.println("Timed out waiting");
                throw new RuntimeException("Timed out waiting");
            }
        };
    }


}
