package zk;

import static zk.Node.Type.Ephemeral;
import static zk.Node.Type.Peristent;

public class ZKTester {


    public static void main(String[] args) {


        Cluster cluster = new Cluster();



        Server server = new Server("zk1");
        Server server1 = new Server("zk2");

        cluster.register(server);
        cluster.register(server1);

        ZKClient client = new ZKClient(cluster);

        client.addWatch("/");

        client.add("/","/test/" , Peristent);

        client.add("/","/test1/" , Ephemeral);

        client.add("/","/home1/" , Ephemeral);

        client.delete("/home1/" );

        client.add("/","/test/" , Peristent);

        client.add("/","/test1/" , Ephemeral);

        client.add("/","/home1/" , Ephemeral);

     /*   ZKClient client1 = new ZKClient(cluster);
        client1.add("/","/test/",Peristent); */


        server.persist();
      //  server1.persist();



    }
}
