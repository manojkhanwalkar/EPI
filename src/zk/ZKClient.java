package zk;

import static zk.Node.Type.Peristent;

public class ZKClient {

    Cluster cluster ;

    volatile Server server ;

    public ZKClient(Cluster cluster)
    {
        this.cluster = cluster;

        this.server = cluster.getServer();
    }


    public boolean add(String path , String name , Node.Type type)
    {
        Node test = new Node();
        test.type= type;
        test.name = name;

        return server.add(path ,test);
    }


    public boolean  delete(String path)
    {

        return server.delete(path);
    }


    public void addWatch(String path)
    {
        server.add(path,this);

    }

    public void deleteWatch(String path)
    {
        server.delete(path);
    }

    public void watchMessage(Server.NodeAction action, String path)
    {
        System.out.println(action + " " + path);
    }

}
