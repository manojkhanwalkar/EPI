package zk;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ZKTree {


    Node root;
    public void init()
    {
        root = new Node();
        root.name = "/";
        root.type = Node.Type.Peristent;

        root.parent=null;

        nodeMap.put(root.name,root);
    }

    Map<String,Node> nodeMap = new HashMap<>();


    public ZKTree()
    {

    }


    public boolean add(String path, Node node)
    {
        Node parent = nodeMap.get(path);
        if (parent==null)
            return false;

        if (parent.children.contains(node))
            return false;

        parent.children.add(node);
        node.parent = parent;


        nodeMap.put(node.name,node);

        return true;


    }


    public Node get(String path)
    {
        Node curr = nodeMap.get(path);

        if (curr!=null)
            return Node.copy(curr);

        return null;


    }

    public boolean delete(String path)
    {
        // the node and all its underlying children will be deleted by removing the association with the parent.
        Node node = nodeMap.get(path);
        if (node==null)
        {
            return false;
        }

        //nodeMap.remove(path);

      //  node.parent.children.remove(node);

        // remove node and children from the node map.

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        while(!queue.isEmpty())
        {
            Node curr = queue.remove();
            curr.parent.children.remove(node);
            nodeMap.remove(curr.name);
            if (!curr.children.isEmpty())
            {
                queue.addAll(curr.children);
            }
        }

        return true;

    }

}
