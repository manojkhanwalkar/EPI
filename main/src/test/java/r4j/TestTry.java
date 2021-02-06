package test.java.r4j;

import org.junit.Test;
import r4j.run.Try;

public class TestTry {

    @Test
    public void testRunnable()
    {
        Try.run(()->{System.out.println("Hello from runnable");});

    }
    @Test
    public void testSupplier()
    {

      var result =   Try.ofSupplier(()->{return "Hello from supplier";});

      System.out.println(result.get());

    }



}
