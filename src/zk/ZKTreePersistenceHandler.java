package zk;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ZKTreePersistenceHandler {


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
            persistentNodes = (List<Node>)ois.readObject();

        } catch(Exception ex) { //ex.printStackTrace();
             }


        if (persistentNodes!=null)
           persistentNodes.forEach(System.out::println);



        ZKTree tree =  new ZKTree(); //TODO - get tree from recovered nodes

        tree.init();

        return tree;

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
