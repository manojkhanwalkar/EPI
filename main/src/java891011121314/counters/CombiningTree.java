package java891011121314.counters;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Stack;

import static java891011121314.counters.CombiningTree.Node.Status.*;

public class CombiningTree {


    static class Node {
        public enum Status {Idle, Root, First, Second, Result}

        ;

        int first;
        int second;
        int result;

        Node parent;

        volatile boolean locked;

        Status status;

        public Node() {
            locked = false;
            status = Root;
            parent = null;
        }

        public Node(Node myParent) {
            parent = myParent;
            locked = false;
            status = Idle;

        }

        synchronized boolean precombine() {


            while (locked) { wt() ; }
            switch (status) {
                case Idle:
                    status = First;
                    return true;
                case First:
                    status = Second;
                    return false;
                case Root:
                    return false;

                default:
                    throw new RuntimeException("Invalid state");
            }

        }

        synchronized int combine(int combined) {
            while (locked) { wt() ; }
            locked = true;
            first = combined;
            switch(status)
            {
                case First:
                    return first;
                case Second:
                    return second+first;
                default:
                    throw new RuntimeException("Invalid state");

            }

        }

        synchronized int op(int combined)
        {
            switch(status)
            {
                case Root:
                    int prior = result;
                    result+=combined;
                    return prior;
                case Second:
                    second = combined;
                    locked = false;
                    notifyAll();
                    while(status!=Result) wt();
                    locked=false;
                    notifyAll();
                    status = Idle;
                    return result;
                default:
                    throw new RuntimeException("Invalid state");


            }
        }


        synchronized void distribute(int prior)
        {
            switch(status)
            {
                case First:
                    status=Idle;
                    locked = false;
                    break;
                case Second:
                    result = prior+first;
                    status = Result;
                    break;
                default :
                    throw new RuntimeException("Invalid status");
            }

            notifyAll();
        }


        private void wt() {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        }


    }


    Node[] nodes;
    Node[] leaf;

    public CombiningTree(int width) {
        nodes = new Node[width - 1];
        nodes[0] = new Node();  // root ;
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node(nodes[(i - 1) / 2]);
        }

        leaf = new Node[(width + 1) / 2];
        for (int i = 0; i < leaf.length; i++) {
            leaf[i] = nodes[nodes.length - 1 - i];
        }

        System.out.println("Number of nodes " + nodes.length);
        System.out.println("Number of leaves " + leaf.length);

    }


    public int getAndIncrement(int leafToUse)
    {
        Stack<Node> stack = new Stack<>();
        Node myLeaf = leaf[leafToUse];
        Node node = myLeaf;

        while(node.precombine())
        {
            node = node.parent;
        }

        Node stop = node;

        int combined = 1 ;

        node = myLeaf;
        while(node!=stop)
        {
            combined = node.combine(combined);
            stack.push(node);
            node = node.parent;
        }

        int prior = stop.op(combined);

        while(!stack.isEmpty())
        {
            node = stack.pop();
            node.distribute(prior);
        }

        return prior;
    }





}
