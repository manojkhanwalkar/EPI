package zk;

import io.netty.util.internal.ConcurrentSet;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ZKTreePersistenceHandler {

    static final ObjectInputFilter RECOVERY_FILTER = ObjectInputFilter.Config.createFilter(
            "maxdepth=20;maxrefs=1000000;maxbytes=104857600;maxarray=1000000;"
                    + "zk.Node;zk.Node$Type;java.util.ArrayList;java.util.HashMap;java.util.LinkedHashMap;"
                    + "java.util.TreeMap;java.util.HashSet;java.util.concurrent.ConcurrentHashMap;java.lang.Enum;java.lang.String;java.lang.Number;java.lang.Integer;java.lang.Long;"
                    // array classes are matched on their element type (ArrayList/HashMap backing arrays)
                    + "java.lang.Object;java.util.Map$Entry;!*");


    public static void persist(String fileName, ZKTree tree)
    {
        try (FileOutputStream fos = new FileOutputStream(new File(fileName));
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            List<Node> persistentNodes = tree.nodeMap.values().stream().filter(node -> node.type== Node.Type.Peristent).collect(Collectors.toList());

            oos.writeObject(persistentNodes);
            oos.flush();

        } catch(Exception ex) { ex.printStackTrace(); }

    }


    public static ZKTree recover(String fileName)
    {
        List<Node> persistentNodes = null;
        // recover list and print nodes
        try(FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
        )
        {
            // Only allow the types a persisted tree actually contains; anything else
            // (e.g. a deserialization gadget chain planted in the file) is rejected.
            ois.setObjectInputFilter(RECOVERY_FILTER);
            persistentNodes = (List<Node>)ois.readObject();

        } catch (FileNotFoundException ex) {
            // no snapshot yet - start with a fresh tree
        } catch(Exception ex) {
            System.err.println("Could not recover ZK tree from " + fileName + ", starting fresh: " + ex);
        }


        if (persistentNodes!=null)
           persistentNodes.forEach(System.out::println);



        ZKTree tree =  new ZKTree();
        if (persistentNodes==null) {

            tree.init();
        }
        else {

            persistentNodes.forEach(node -> {


                node.children = new ConcurrentSet<>();
                tree.nodeMap.put(node.name, node);


            });

            persistentNodes.forEach(node -> {

                if (node.name.equals("/")) {
                    // root node - no parent
                } else {
                    String parent = findParent(node.name);
                    Node parentNode = tree.nodeMap.get(parent);
                    parentNode.children.add(node);
                    node.parent = parentNode;
                }
            });

        }

        return tree;

    }

    private static String findParent(String path)
    {
        for (int i=path.length()-2;i>=0;i--)
        {
            if (path.charAt(i)=='/')
            {
                return path.substring(0,i+1);
            }
        }

        System.out.println("Parent not located " + path);
        return null;
    }

    /*
      ArrayList<WriteObject> woi=new ArrayList<>();
    try {
        FileOutputStream fop=new FileOutputStream("c://object.ser");
        ObjectOutputStream oos=new ObjectOutputStream(fop);
        woi.add(wo);
        woi.add(wo1);
        oos.writeObject(woi);

    } catch NotFoundException e) {
}
     */

}
