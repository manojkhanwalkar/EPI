package rfs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Collectors;

public class INodeWriter implements Runnable {

    FileSystem fileSystem;
    public INodeWriter(FileSystem fileSystem)
    {
        this.fileSystem = fileSystem;
    }


    public void run()
    {
        try {
            fileSystem.dataFileReadWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FileSystem.directory+FileSystem.inodeFile)))
        {

            INode root = fileSystem.inodeMap.get("/");

            Queue<INode> dirs = new ArrayDeque<>();
            dirs.add(root);

            while(!dirs.isEmpty())
            {
                INode curr =  dirs.remove();

                try {
                    writer.write(INode.INodeSerialized(curr));
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
