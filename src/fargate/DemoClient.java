package fargate;

import java.time.Instant;

public class DemoClient {

    public static void main(String[] args) throws Exception {

        while(true)
        {
            System.out.println(Instant.now());

            Thread.sleep(1000);
        }


    }

}
