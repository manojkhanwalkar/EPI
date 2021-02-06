package r4j.run;

import r4j.circuitbreaker.CircuitBreaker;
import r4j.ratelimiter.RateLimiter;
import r4j.retry.Retry;

import java.util.function.Supplier;

public class Decorator {


    public static <T> DecoratorBuilder<T> ofSupplier(Supplier<T> supplier)
    {
        return new DecoratorBuilder<>(supplier);
    }


    public static class DecoratorBuilder<T>
    {
        Supplier<T> supplier;
        public DecoratorBuilder(Supplier<T> supplier) {

            this.supplier = supplier;
        }

       public DecoratorBuilder<T> withCircuitBreaker(CircuitBreaker circuitBreaker)
        {
            supplier = CircuitBreaker.decorateSupplier(circuitBreaker,supplier);
            return this;
        }

        public DecoratorBuilder<T> withRateLimiter(RateLimiter rateLimiter)
        {
            supplier = RateLimiter.decorateSupplier(rateLimiter,supplier);
            return this;
        }

        public DecoratorBuilder<T> withRetry(Retry retry)
        {
            supplier = Retry.decorateSupplier(retry,supplier);
            return this;
        }

        public Supplier<T> decorate()
        {
            return supplier;
        }
    }
}
