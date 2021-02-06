package r4j.ratelimiter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RateLimiter {

    int duration;
    int permits;
    int timeout;

    Semaphore semaphore ;

    ScheduledExecutorService scheduler ;

    public RateLimiter(RateLimiterConfig config)
    {
        this.duration = config.getDuration();
        this.permits = config.getPermits();
        this.timeout = config.getTimeout();

        semaphore = new Semaphore(permits,true);

        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleAtFixedRate(this::refresh,duration,duration, TimeUnit.SECONDS);
    }


    public void refresh()
    {
        int permissionsToRelease =
                permits - semaphore.availablePermits();
        semaphore.release(permissionsToRelease);
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

    public static <T> Supplier<T> decorateSupplier(RateLimiter rateLimiter , Supplier<T> supplier)
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
                }
            }
            else
                {
                throw new RuntimeException("Timed out waiting");
            }
        };
    }


}
