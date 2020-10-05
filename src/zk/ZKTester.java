package zk;

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

        for (int i=0;i<5;i++) {

            CompletableFuture.runAsync(() -> {

                client.add("/", "/test/", Peristent);

                client.add("/", "/app1/", Ephemeral);

                client.add("/", "/home1/", Ephemeral);


            }).join();


      /*      CompletableFuture.runAsync(() -> {

                client1.delete("/test/");

                client1.delete("/app1/");

                client1.delete("/home1/");


            });*/

        }




        server.persist();
        server1.persist();



    }
}
