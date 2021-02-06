package r4j.circuitbreaker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class CircuitBreaker {

    int waitDuration;
    int percentageError;
    int numberOfEvents;

    static boolean Closed = true;
    static boolean Open = false;
    AtomicBoolean state = new AtomicBoolean(Closed);

    ConcurrentLinkedQueue<Event> queue = new ConcurrentLinkedQueue<>();

    AtomicInteger good = new AtomicInteger(0);
    AtomicInteger bad = new AtomicInteger(0);

    static enum Event { Good , Bad};

    public CircuitBreaker(CircuitBreakerConfig config)
    {
        this.waitDuration = config.getWaitDuration();
        this.percentageError = config.getPercentageError();
        this.numberOfEvents = config.getNumberOfEvents();
    }



    private boolean acquirePermission() {
        if (state.get()==Closed)
            return true;
        else
            return false;
    }

    private void processState(Event event)
    {
        // process the current event
        queue.add(event);
        if (event== Event.Bad)
            bad.incrementAndGet();
        else
            good.incrementAndGet();

        int size = queue.size();
        if (size <= numberOfEvents)
            return ;
        Event old = queue.remove();
        if (old== Event.Bad)
            bad.decrementAndGet();
        else
            good.decrementAndGet();

        if((state.get()==Closed) && ((bad.get()/good.get())*100>percentageError))
        {
           state.set(Open);
           CompletableFuture.runAsync(()->{
               try {
                   Thread.sleep(waitDuration*1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               bad.set(0);
               good.set(0);
               queue.clear();
               state.set(Closed);
           });
        }

    }

    public static <T> Supplier<T> decorateSupplier(CircuitBreaker circuitBreaker , Supplier<T> supplier)
    {

          return () -> {

            boolean permission = circuitBreaker.acquirePermission();
            if (permission) {
                try {
                    T result =supplier.get();

                    circuitBreaker.processState(Event.Good);
                    return result;
                    //  rateLimiter.onSuccess();
                } catch (Exception exception) {
                    //rateLimiter.onError(exception);
                    circuitBreaker.processState(Event.Bad);
                    throw exception;
                }
            }
            else
                {
                throw new RuntimeException("Circuit is open");
            }
        };
    }


}
