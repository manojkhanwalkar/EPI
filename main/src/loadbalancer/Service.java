package loadbalancer;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Service {

    String id = UUID.randomUUID().toString();

    static ThreadLocalRandom random = ThreadLocalRandom.current();
    public void process()
    {

       int sleep =  random.nextInt(1000, 10000);

       System.out.println("Service " + id + " called , sleeping for " + sleep);

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
