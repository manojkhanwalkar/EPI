package dht;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster {

    public static int Hash = 1024;


    List<Node> sortedNodes = new ArrayList<>();

    //TODO - inform other nodes on add / delete

    public void add(Node node)
    {
        sortedNodes.add(node);
        Collections.sort(sortedNodes);

        if (sortedNodes.size()==1)  // first node - nothing to migrate .
            return;

        int index = sortedNodes.indexOf(node);

        int prevLocation = (index==0)?sortedNodes.get(sortedNodes.size()-1).location: sortedNodes.get(index-1).location;

        Node nextNode = (index==sortedNodes.size()-1) ? sortedNodes.get(0) : sortedNodes.get(index+1);

        Node prevNode = (index==0) ? sortedNodes.get(sortedNodes.size()-1) : sortedNodes.get(index-1);

        if (prevLocation<node.location) {
            List<Dummy> objects = nextNode.migrate(prevLocation, node.location);
            objects.forEach(o->node.add(o));

            objects = prevNode.migrateReplicas(prevLocation, node.location);
            objects.forEach(o->node.addReplica(o));

        }
        else // have to cater for circle
        {
            List<Dummy> objects = nextNode.migrate(prevLocation, Hash);
            objects.forEach(o->node.add(o));
            objects = nextNode.migrate(0, node.location);
            objects.forEach(o->node.add(o));

            objects = prevNode.migrateReplicas(prevLocation, Hash);
            objects.forEach(o->node.addReplica(o));
            objects = prevNode.migrateReplicas(0, node.location);
            objects.forEach(o->node.addReplica(o));

        }




    }


    public void delete(Node node)
    {
        int index = sortedNodes.indexOf(node);
        int next = (index==sortedNodes.size()-1)  ? 0 : index+1;

        int prev = (index==0) ? sortedNodes.size()-1 : index-1;

        Node nextNode = sortedNodes.get(next);
        List<Dummy> objects = node.move();
        objects.forEach(o->nextNode.add(o));

        Node prevNode = sortedNodes.get(prev);
         objects = node.moveReplicas();
        objects.forEach(o->prevNode.addReplica(o));


        sortedNodes.remove(node);



        Collections.sort(sortedNodes);


    }

    public NodeReplicaTuple getNode(Dummy dummy)
    {
        String key = dummy.key;

        int hash = Math.abs(key.hashCode());
        int location = hash%Hash;

       dummy.location = location;

       NodeReplicaTuple tuple = new NodeReplicaTuple();

        if (location > sortedNodes.get(sortedNodes.size()-1).location)
        {
            tuple.node= sortedNodes.get(0);
            tuple.replica = sortedNodes.get(sortedNodes.size()-1);
        }
        else
        {
            int index= getIndex(location);
             tuple.node = sortedNodes.get(index);
             if (index!=0)
                tuple.replica = sortedNodes.get(index-1);
             else
                 tuple.replica = sortedNodes.get(sortedNodes.size()-1);
        }

        return tuple;
    }

    private int getIndex(int location)
    {
        // get index of node equal or nearest greater.
        int start=0; int end = sortedNodes.size()-1;
        while(true)
        {
            int mid = start + (end-start)/2;
            Node midNode = sortedNodes.get(mid);
            if (midNode.location == location)
                return mid;

            if (mid==0 || mid==sortedNodes.size()-1)
                return mid;
            Node prevNode = sortedNodes.get(mid-1);
            if (midNode.location > location && prevNode.location < location)
                return mid;

            if (location > midNode.location)
            {
                start = mid+1;
            }
            else
            {
                end = mid-1;
            }
        }
    }


    static class NodeReplicaTuple
    {
        Node node ;
        Node replica;


        public void add(Dummy dummy) {

            node.add(dummy);
            replica.addReplica(dummy);
        }
    }

    public static void main(String[] args) {

        Cluster cluster = new Cluster();

        Node node1 = new Node("0");

        Node node2 = new Node("1");
        Node node3 = new Node("2");
        Node node4 = new Node("3");

        cluster.add(node1);
        cluster.add(node2);

        cluster.print();




         for (int i=0;i<2;i++)
          {
              Dummy dummy = new Dummy();

              NodeReplicaTuple tuple = cluster.getNode(dummy);

              tuple.add(dummy);

           }


        cluster.add(node3);
        cluster.add(node4);

        cluster.print();



        cluster.delete(node2);
        cluster.delete(node3);
        cluster.delete(node4);

        cluster.print();


    }

    private void print() {

        sortedNodes.forEach(System.out::println);

        System.out.println();
    }

}
