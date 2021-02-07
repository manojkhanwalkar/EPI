package r4j.timelimiter;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class TimeLimiter {

    // TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

    //  String result1 = timeLimiter.executeFutureSupplier(
    //                () -> CompletableFuture.supplyAsync(() -> "hello world"));

    private TimeLimiter(Duration duration)
    {
        this.duration = duration;
    }
    Duration duration;
    public static TimeLimiter of(Duration duration)
    {
        return new TimeLimiter(duration);
    }


     public <T, F extends Future<T>> T executeFutureSupplier(Supplier<F> futureSupplier) throws Exception {
        return decorateFutureSupplier(futureSupplier).call();
    }


    public  <T, F extends CompletionStage<T>> CompletionStage<T> executeCompletionStage(ScheduledExecutorService scheduler, Supplier<F> supplier) {
        return (CompletionStage)decorateCompletionStage( scheduler, supplier).get();
    }

    static ScheduledFuture<?> timeout(CompletableFuture<?> future, ScheduledExecutorService scheduler, long delay, TimeUnit unit) {
        return scheduler.schedule(() -> {
            if (future != null && !future.isDone()) {
                //future.completeExceptionally(TimeLimiter.createdTimeoutExceptionWithName(name, (Throwable)null));
                future.completeExceptionally(null);
            }

        }, delay, unit);
    }

    public <T, F extends CompletionStage<T>> Supplier<CompletionStage<T>> decorateCompletionStage(ScheduledExecutorService scheduler, Supplier<F> supplier) {
        return () -> {
            CompletableFuture<T> future = ((CompletionStage)supplier.get()).toCompletableFuture();
            ScheduledFuture<?> timeoutFuture = timeout(future, scheduler,this.duration.toMillis(), TimeUnit.MILLISECONDS);
            return future.whenComplete((result, throwable) -> {
                if (result != null) {
                    if (!timeoutFuture.isDone()) {
                        timeoutFuture.cancel(false);
                    }

                }

                if (throwable != null) {
                    Throwable cause;
                    if (throwable instanceof CompletionException) {
                        cause = throwable.getCause();
                        //this.onError(cause);
                    } else if (throwable instanceof ExecutionException) {
                        cause = throwable.getCause();
                        if (cause == null) {
                            //this.onError(throwable);
                        } else {
                            //this.onError(cause);
                        }
                    } else {
                        //this.onError(throwable);
                    }
                }

            });
        };
    }



    public <T, F extends Future<T>> Callable<T> decorateFutureSupplier(Supplier<F> futureSupplier) {
        return () -> {

            Future<T> future = futureSupplier.get();
            try {
                T result = future.get(this.duration.getSeconds(), TimeUnit.SECONDS);
              return result;
            } catch (TimeoutException timeoutException) {

                System.out.println("Future cancelled");
                future.cancel(true);
                throw timeoutException;
            }

        };
    }


}
