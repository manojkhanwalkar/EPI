package dht;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dht.Cluster.Hash;

public class Node implements Comparable<Node> {

    String name ;


    int location;

    public Node(String name)
    {
        this.name = name;

        String id = UUID.randomUUID().toString();

        int hash = Math.abs(id.hashCode());

        location = hash%Hash;

    }

    Set<Dummy> values = new HashSet<>();

    Set<Dummy> replicas = new HashSet<>();

    public void add(Dummy dummy)
    {
        values.add(dummy);
    }

    public void addReplica(Dummy dummy)
    {
        replicas.add(dummy);
    }


    public List<Dummy> move()
    {
        return new ArrayList<>(values);
    }


    public List<Dummy> moveReplicas()
    {
        return new ArrayList<>(replicas);
    }


    // called when a new node is added .
    public List<Dummy> migrate(int start, int end)
    {

       // System.out.println("Migrating  "+ this.name + "  " + start + "  " + end);

        Predicate<Dummy> migrateCondition = (dummy )-> {  return (start < dummy.location && dummy.location <=end) ;  };
       List<Dummy> migratingObjects =  values.stream().filter(migrateCondition).collect(Collectors.toList());

       List<Dummy> stayingObjects = values.stream().filter(migrateCondition.negate()).collect(Collectors.toList());

       values.clear();

       values.addAll(stayingObjects);

       return migratingObjects;


    }

    // called when a new node is added . Replicas collection is reverse of the node
    public List<Dummy> migrateReplicas(int start, int end)
    {


        Predicate<Dummy> migrateCondition = (dummy )-> {  return (start < dummy.location && dummy.location <=end) ;  };


        List<Dummy> migratingObjects =  replicas.stream().filter(migrateCondition).collect(Collectors.toList());

        List<Dummy> stayingObjects = replicas.stream().filter(migrateCondition.negate()).collect(Collectors.toList());

        replicas.clear();

        replicas.addAll(migratingObjects);

        return stayingObjects;


    }


    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.location, node.location);
    }


    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", values=" + values +
                "  replicas=" + replicas +
                '}';
    }
}
