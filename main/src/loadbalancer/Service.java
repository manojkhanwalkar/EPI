package loadbalancer;

import java.security.SecureRandom;
import java.util.UUID;

public class Service {

    String id = UUID.randomUUID().toString();

    static SecureRandom random = new SecureRandom();
    public void process()
    {

       int sleep =  random.nextInt(10000);

       System.out.println("Service " + id + " called , sleeping for " + sleep);

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
