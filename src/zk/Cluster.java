package zk;

import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Cluster {

    volatile Set<Server> servers = new ConcurrentSet<>();



    public Cluster()
    {

    }

    public void register(Server server)
    {
        servers.add(server);

        List<Server> tmp =  new ArrayList<>(servers);

        servers.stream().forEach(s -> s.updateServersInCluster(tmp));


    }


    public Server getServer()
    {
        return servers.parallelStream().findAny().get();
    }




}
