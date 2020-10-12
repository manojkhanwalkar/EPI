package hbase;

public class ShutDownProcessor  implements Runnable {

    Node node;

    public ShutDownProcessor(Node node) {
        this.node = node;
    }


    public void run()
    {
        if (node.records.size()!=0)
        {
            node.manager.persist(node.records);
        }
    }
}
