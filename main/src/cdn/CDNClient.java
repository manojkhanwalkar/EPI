package cdn;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CDNClient {


    final static String[] sites = {"https://www.yahoo.com", "https://time.com" , "http://www.google.com"};

    static String[] files = {"words1.txt","words2.txt", "words3.txt" } ;

    static String dir = "/home/manoj/data/";

    public static void main(String[] args) {

          testFiles();


         // testURLs();

    }

    public static void testFiles()
    {

        test(new FileServer(),files,dir);
    }


    public static void testURLs() {

        test(new URLServer(), sites, "");

    }

    public static void test(Server server, String[] options , String url)
    {
        CDNCache cache = new CDNCache(server);

        CompletableFuture<Void> []  futures = new CompletableFuture[5];

        for (int k=0;k<5;k++) {

            futures[k] = CompletableFuture.runAsync(() -> {

                for (int i = 0; i < 50; i++) {

                    System.out.println("Round " + i);

                    Arrays.stream(options).forEach(urlExtension -> {

                        String s = cache.get(url, urlExtension);


                    });


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            });

        }

        CompletableFuture.allOf(futures).join();


    }
}
