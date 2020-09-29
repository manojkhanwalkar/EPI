package util;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

public class Connection {

    String target;

    HttpClient client ;





    private void initConnectionProps()
    {
        final Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());

    }

    public Connection(String target)
    {
        this.target = target;
        initConnectionProps();
        client = HttpClient.newHttpClient();
    }



    public Optional<String> sendSimple(String request,String action)
    {

            return post(request,action);

    }




    private Optional<String> post(String str , String url) //throws Exception
    {

        try {
            HttpRequest httprequest = HttpRequest.newBuilder()
                    .uri(URI.create(target + url))
                    .header("Content-Type", "application/json")
                   //  .header("HMAC" , HMACUtil.calculateHMAC(str,HmacKey))
                    .POST(HttpRequest.BodyPublishers.ofString(str))
                    .timeout(Duration.ofMinutes(1))
                    .build();


            HttpResponse<String> response =
                    client.send(httprequest, HttpResponse.BodyHandlers.ofString());


            System.out.println(response);

           if (response.statusCode()==200)
               return Optional.of(response.body());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }



    public HttpResponse<String> get( String url)
    {

        try {
            HttpRequest httprequest = HttpRequest.newBuilder()
                    .uri(URI.create(target + url))
                    .GET()
                    .timeout(Duration.ofMinutes(1))
                    .build();


            HttpResponse<String> response =
                    client.send(httprequest, HttpResponse.BodyHandlers.ofString());

            return response;

//            return response.body();

        } catch (Exception ex)
        {
            return null;
        }


    }




}
