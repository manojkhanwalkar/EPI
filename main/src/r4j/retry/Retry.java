package r4j.retry;

import lombok.SneakyThrows;

import java.util.function.Supplier;

public class Retry {

    int backoff;
    int numRetries;
    int waitTime;



    public Retry(RetryConfig config)
    {
        this.backoff = config.getBackoff();
        this.numRetries = config.getNumRetries();
        this.waitTime = config.getWaitTime();


    }



    @SneakyThrows
    public static <T> Supplier<T> decorateSupplier(Retry retry , Supplier<T> supplier)
    {

          return () -> {
              int waitTime = retry.waitTime;
            for (int i=0;i<retry.numRetries;i++) {
                    try {
                        return supplier.get();
                    } catch (Exception exception) {

                        System.out.println("Error - retrying");
                        try {
                            Thread.sleep(waitTime*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waitTime = waitTime+ retry.backoff;

                    }
                }

            throw new RuntimeException("Number of retries exceeded");

        };
    }


}
