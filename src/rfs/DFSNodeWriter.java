package rfs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class DFSNodeWriter implements Runnable {

    DFS dfs;
    public DFSNodeWriter(DFS dfs)
    {
        this.dfs = dfs;
    }


    public void run()
    {
        dfs.fileSystems.forEach(fileSystem -> {

            new INodeWriter(fileSystem).run();

        });




        try(BufferedWriter writer = new BufferedWriter(new FileWriter(DFS.dfsNodeFile)))
        {

            DFSNode root = dfs.inodeMap.get("/");

            Queue<DFSNode> dirs = new ArrayDeque<>();
            dirs.add(root);

            while(!dirs.isEmpty())
            {
                DFSNode curr =  dirs.remove();

                try {
                    writer.write(DFSNode.DFSNodeSerialized(curr));
                    writer.newLine();
                    writer.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (curr.getNodes()!=null)
                {
                    dirs.addAll(curr.getNodes());
                }
            }



            writer.flush();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
