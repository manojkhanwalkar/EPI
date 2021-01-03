package zk;

public class Counter {


    String name ;
    ZKClient client;


    public Counter(String name , ZKClient client)
    {
        this.name = name ;
        this.client = client;
        client.add("/" ,name, Node.Type.Ephemeral);
    }

    public int get()
    {
        return client.get(name).version;
    }

    public int getAndIncrement()
    {
        int res = client.get(name).version;
        while(!client.update(name,res,null))
        {
            res = client.get(name).version;
        }

        return res;
    }
}
