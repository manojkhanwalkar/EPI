package zk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static zk.Node.Type.Ephemeral;
import static zk.Node.Type.Peristent;

public class ZKTester {


    public static void main(String[] args) throws Exception {


        Cluster cluster = new Cluster();



        Server server = new Server("zk1");
        Server server1 = new Server("zk2");

        cluster.register(server);
        cluster.register(server1);

        ZKClient client = new ZKClient(cluster);

        client.addWatch("/test/");

        ZKClient client1 = new ZKClient(cluster);

        client1.addWatch("/home1/");

        client.add("/", "/test/", Peristent);


        Node node = client.get("/test/");

        System.out.println(node);

        Map<String,String> attributes = new HashMap<>();
        attributes.put("Key", "Values");

        client.update("/test/",node.version,attributes);

        node = client.get("/test/");

        System.out.println(node);


      /*  for (int i=0;i<5;i++) {

            CompletableFuture.runAsync(() -> {

                client.add("/", "/test/", Peristent);

                client.add("/", "/app1/", Ephemeral);

                client.add("/", "/home1/", Ephemeral);


            }).join(); */


      /*      CompletableFuture.runAsync(() -> {

                client1.delete("/test/");

                client1.delete("/app1/");

                client1.delete("/home1/");


            });

        } */



     /*   final String lockStr = "/applock1/";

        var x1 = CompletableFuture.runAsync(()->{

        int count=0;


        for (int i=0;i<500;i++)
        {
            client1.lock(lockStr);
            System.out.println("Client 1 has lock " + count++);
            client1.unlock(lockStr);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        });



      var x2 =   CompletableFuture.runAsync(()->{

          int count=0;

            for (int i=0;i<500;i++)
        {
            client.lock(lockStr);
            System.out.println("Client 22222222222  has lock " + count++);
            client.unlock(lockStr);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



        });

      CompletableFuture.allOf(x1,x2).join();
*/

        server.persist();
        server1.persist();

    }
}
