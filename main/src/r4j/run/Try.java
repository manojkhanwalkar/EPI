package r4j.run;

import java.util.function.Supplier;

public class Try<T> {

    public static void run(Runnable runnable)
    {
        runnable.run();
    }

    T t ;

    private Try(T t)
    {
        this.t=t;
    }

    public T get()
    {
        return t;
    }


    public static <T> Try<T> ofSupplier(Supplier<T> supplier)
    {
        T result = supplier.get();
        return new Try<T>(result);
    }


}
