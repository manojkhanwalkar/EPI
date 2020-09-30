package rfs;

import java891011121314.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DFSNode {



    public DFSNode()
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

    List<DFSNode> nodes ;  // used by dir

    List<DFSDataBlockMeta> dataBlockMetas ; // used to describe the datablocks if a file




    public void setDirOrFile(boolean dirOrFile) {
        this.dirOrFile = dirOrFile;
    }

    public String getFqpn() {
        return fqpn;
    }

    public void setFqpn(String fqpn) {
        this.fqpn = fqpn;
    }

    public List<DFSNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<DFSNode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(DFSNode inode) {
        if (nodes==null)
        {
            nodes = new ArrayList<>(1);

        }
        nodes.add(inode);
    }



    public static String  DFSNodeSerialized(DFSNode node)
    {
        return JSONUtil.toJSON(new SerializedDFSNode(node));
    }

    public void addDataBlockMeta(DFSDataBlockMeta meta) {

        if (dataBlockMetas==null)
        {
            dataBlockMetas = new ArrayList<>();
        }
        dataBlockMetas.add(meta);


    }


    static class SerializedDFSNode
    {
        boolean dirOrFile;     // dir = true , file = false
        String fqpn;

        List<String> nodeNames ;  // used by dir

        List<DFSDataBlockMeta> dataBlockMeta ;   // used by file for data blocks location .



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


        public List<DFSDataBlockMeta> getDataBlockMeta() {
            return dataBlockMeta;
        }

        public void setDataBlockMeta(List<DFSDataBlockMeta> dataBlockMeta) {
            this.dataBlockMeta = dataBlockMeta;
        }

        public SerializedDFSNode(DFSNode node)
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

        public SerializedDFSNode() {}
    }
}
