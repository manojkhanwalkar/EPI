package fargate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProcessDemo {

    public static void main(String[] args) throws Exception {

        Runtime r = Runtime.getRuntime();
     //   Process p = r.exec("java -cp /home/manoj/IdeaProjects/EPI/target/EPI-1.0-SNAPSHOT.jar fargate.DemoClient");

        Process p = r.exec("java -cp  /home/manoj/IdeaProjects/JWT/target/JWT-1.0-SNAPSHOT.jar services.authorizer.AuthApplication server /home/manoj/IdeaProjects/JWT/src/main/resources/Auth.yml" );
        CompletableFuture.runAsync(()->{

            try {
                p.waitFor(65, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.destroy();

        });

        BufferedReader br=new BufferedReader(
                new InputStreamReader(
                        p.getInputStream()));
        String line;
        while((line=br.readLine())!=null){
            System.out.println(line);
        }







    }
}