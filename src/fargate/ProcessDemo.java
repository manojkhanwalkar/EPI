package fargate;

import java.util.concurrent.CompletableFuture;

public class ProcessDemo {

    public static void main(String[] args) throws Exception {

        while(true)
        {
            Process p = process();
            while(p.isAlive())
            {
                Thread.sleep(1000);
            }

        }

    }

    public static Process process() throws Exception  {

        Runtime r = Runtime.getRuntime();
     //   Process p = r.exec("java -cp /home/manoj/IdeaProjects/EPI/target/EPI-1.0-SNAPSHOT.jar fargate.DemoClient");

        Process p = r.exec("java -cp  /home/manoj/IdeaProjects/JWT/target/JWT-1.0-SNAPSHOT.jar services.authorizer.AuthApplication server /home/manoj/IdeaProjects/JWT/src/main/resources/Auth.yml " );

        System.out.println(p.pid());

        System.out.println(p.info());





        CompletableFuture.runAsync(()->{

            try {
                p.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);

                Runtime.getRuntime().exec("kill -SIGINT " + p.pid());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //p.destroy();

        });


        StreamProcessor streamProcessor = new StreamProcessor(p.getInputStream(),System.out::println);

        CompletableFuture.runAsync(streamProcessor);

        return p;


    }
}