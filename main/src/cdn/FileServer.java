package cdn;

import util.Connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class FileServer implements Server{




    public CompletableFuture<String> getFromServer(String url, String urlExtension)
    {

        System.out.println("Getting from File System ");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            StringBuilder builder = new StringBuilder();

            try(BufferedReader reader = new BufferedReader(new FileReader(url+urlExtension)))
           {

               String response ;

               while((response= reader.readLine())!=null)
               {
                   builder.append(response);
                   builder.append("\n");
               }

           } catch(Exception e) {
               e.printStackTrace();
           }

            return builder.toString();

        });


        return future;



    }
}
