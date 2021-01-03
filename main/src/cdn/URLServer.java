package cdn;

import util.Connection;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class URLServer implements Server  {


    public CompletableFuture<String> getFromServer(String url, String urlExtension)
    {

        System.out.println("Getting from server ");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{

            Connection connection = new Connection(url);


            HttpResponse<String> response = connection.get(urlExtension);


            System.out.println(response);

            return response.body();

        });


        return future;



    }
}
