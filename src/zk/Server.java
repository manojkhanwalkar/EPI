package zk;

import io.netty.util.internal.ConcurrentSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static zk.Server.NodeAction.Add;
import static zk.Server.NodeAction.Delete;

public class Server {

    final static String directory = "/home/manoj/data/zkcluster/";

    ZKTree zkTree;

    String name;

     Set<Server> clusterServers  = new ConcurrentSet<>();

     ConcurrentMap<String,List<ZKClient>>  watchClients = new ConcurrentHashMap<>();

     Lock distributedLock = new DistributedLock(clusterServers);

     Lock treeLock = new ReentrantLock();

     Lock watchLock = new ReentrantLock();

     public enum  NodeAction  { Add , Delete};


    public Server(String name)
    {
        this.name = name;

        zkTree = ZKTreePersistenceHandler.recover(directory+name);

    }


    public void persist()
    {
        ZKTreePersistenceHandler.persist(directory+name,zkTree);
    }

    public boolean  add(String path, Node node)
    {
        try {
            distributedLock.lock();
            if (zkTree.add(path, node))
            {
                replicateAdd(path,node);
                processWatchers(Add, node.name);
                return true;

            }
            else
            {
                return false;
            }
        } finally {

            distributedLock.unlock();


        }

    }

    public void  processAdd(String path, Node node)
    {

            zkTree.add(path, node);
            processWatchers(Add, node.name);


    }

    public boolean delete(String path)
    {
        try {
            distributedLock.lock();
            if (zkTree.delete(path)) {
                replicateDelete(path);

                processWatchers(Delete, path);
                return true;
            }
            else
                return false;
        } finally {

            distributedLock.unlock();

        }


    }


    public void processDelete(String path)
    {

        zkTree.delete(path);
        processWatchers(Delete, path);

    }

    private void replicateDelete( String path)
    {
        Server thisObject = this;
        clusterServers.stream().filter(s->s!=thisObject).forEach(server->server.processDelete(path));
    }

    private void replicateAdd(String path,Node node)
    {
        Server thisObject = this;
        clusterServers.stream().filter(s->s!=thisObject).forEach(server-> {

            Node n = Node.copy(node);
            server.processAdd(path, n);

        });

    }

    private void processWatchers(NodeAction action, String path)
    {
        try {
            watchLock.lock();
            if (action == Delete) {
                watchClients.entrySet().stream().filter(e ->
                        e.getKey().startsWith(path) || path.startsWith(e.getKey())).flatMap(e -> e.getValue().stream()).forEach(client -> {


                            client.watchMessage(action,path);
                });
            }

            if (action == Add) {
                watchClients.entrySet().stream().filter(e ->
                        path.startsWith(e.getKey())).flatMap(e -> e.getValue().stream()).forEach(client -> {


                            client.watchMessage(action,path);
                });
            }
        } finally {
            watchLock.unlock();
        }
    }


    public void add(String path , ZKClient client)
    {
        try {
            watchLock.lock();
            List<ZKClient> watchers = watchClients.computeIfAbsent(path, p -> new ArrayList<>());
            if (!watchers.contains(client))
            {
                watchers.add(client);
            }
        } finally {
            watchLock.unlock();
        }

    }

    public void delete(String path, ZKClient client)
    {
        try {
            watchLock.lock();
            List<ZKClient> watchers = watchClients.get(path);
            if (watchers != null) {
                watchers.remove(client);
            }
            if (watchers.isEmpty()) {
                watchClients.remove(path);
            }
        } finally {
            watchLock.unlock();
        }
    }


    public void updateServersInCluster(List<Server> servers)
    {
        servers.stream().filter(server->!clusterServers.contains(server)).forEach(server-> clusterServers.add(server));
    }




}
