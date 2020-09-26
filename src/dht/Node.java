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

    public void add(Dummy dummy)
    {
        values.add(dummy);
    }


    public List<Dummy> move()
    {
        return new ArrayList<>(values);
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
                '}';
    }
}
