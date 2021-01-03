package cdn;

import util.Connection;

import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public class CDNCache {


    Server server;

    public CDNCache(Server server)
    {
        this.server = server;
    }

    ConcurrentHashMapWithTTL<String,String> map = new ConcurrentHashMapWithTTL<>();

    ConcurrentMap<String,CompletableFuture<String>> futureMap = new ConcurrentHashMap<>();

    public String get(String url, String urlExtension)
    {

        Optional<String>  response = map.get(url+urlExtension);

        if (response.isPresent())
        {
            return response.get();
        }
        else
        {


            String key = url+urlExtension;
            CompletableFuture<String> f = futureMap.computeIfAbsent(key, k-> {

                CompletableFuture<String> future = server.getFromServer(url,urlExtension);
                return future;

            });

            String result = null;
            try {
                result = f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            map.put(key,result);

            futureMap.remove(key);

            return result;
        }



    }



}
