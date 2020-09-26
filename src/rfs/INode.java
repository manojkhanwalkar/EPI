package rfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import java891011121314.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class INode {



    public INode()
    {

    }

    public boolean isDir()
    {
        return dirOrFile;
    }

    public boolean isFile()
    {
        return !dirOrFile;
    }

    boolean dirOrFile;     // dir = true , file = false
    String fqpn;

    List<INode> nodes ;  // used by dir

    List<DataBlockMeta> dataBlockMetas ; // used to describe the datablocks if a file




    public void setDirOrFile(boolean dirOrFile) {
        this.dirOrFile = dirOrFile;
    }

    public String getFqpn() {
        return fqpn;
    }

    public void setFqpn(String fqpn) {
        this.fqpn = fqpn;
    }

    public List<INode> getNodes() {
        return nodes;
    }

    public void setNodes(List<INode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(INode inode) {
        if (nodes==null)
        {
            nodes = new ArrayList<>(1);

        }
        nodes.add(inode);
    }



    public static String  INodeSerialized(INode node)
    {
        return JSONUtil.toJSON(new SerializedINode(node));
    }

    public void addDataBlockMeta(DataBlockMeta meta) {

        if (dataBlockMetas==null)
        {
            dataBlockMetas = new ArrayList<>();
        }
        dataBlockMetas.add(meta);


    }


    static class SerializedINode
    {
        boolean dirOrFile;     // dir = true , file = false
        String fqpn;

        List<String> nodeNames ;  // used by dir

        List<DataBlockMeta> dataBlockMeta ;   // used by file for data blocks location .



        public boolean isDirOrFile() {
            return dirOrFile;
        }

        public void setDirOrFile(boolean dirOrFile) {
            this.dirOrFile = dirOrFile;
        }

        public String getFqpn() {
            return fqpn;
        }

        public void setFqpn(String fqpn) {
            this.fqpn = fqpn;
        }

        public List<String> getNodeNames() {
            return nodeNames;
        }

        public void setNodeNames(List<String> nodeNames) {
            this.nodeNames = nodeNames;
        }


        public List<DataBlockMeta> getDataBlockMeta() {
            return dataBlockMeta;
        }

        public void setDataBlockMeta(List<DataBlockMeta> dataBlockMeta) {
            this.dataBlockMeta = dataBlockMeta;
        }

        public SerializedINode(INode node)
        {
             dirOrFile = node.dirOrFile;
             fqpn = node.fqpn;

             if (node.getNodes()!=null)
                nodeNames  = node.getNodes().stream().map(n->n.fqpn).collect(Collectors.toList());

             if (node.dataBlockMetas!=null)
             {
                 dataBlockMeta = node.dataBlockMetas;
             }


        }

        public SerializedINode() {}
    }
}
