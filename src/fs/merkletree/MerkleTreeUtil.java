package fs.merkletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.RandomIdGenerator;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;


public class MerkleTreeUtil {

    static ObjectMapper mapper ;

    static {
        mapper = new ObjectMapper();
        mapper.enableDefaultTyping();


    }


    public MerkleTree build(File rootDirectory)
    {
        MerkleTree tree = new MerkleTree();
        HashNode root = build(rootDirectory,null);
        tree.setRoot(root);

        return tree;
    }

    private HashNode build(File rootDirectory, HashNode parent)
    {
        HashNode self ;

        if (parent==null) {
            self = new HashNode();
            self.setParent(null);
            self.file = rootDirectory;
            self.id = self.file.getName();
        }
        else
        {
            self = parent;
        }

        String leftHash = buildLeftTree(Arrays.stream(rootDirectory.listFiles())
                .filter(File::isFile)
                .map(f->{
                  HashNode node = new HashNode();
                  node.left = null; node.right=null;
                  node.file = f;
                  node.id = node.file.getName();
                  node.hash = getSHA2HexValue(f.getAbsolutePath()); //TODO - fix this with file contents MD
                  return node;
                }).collect(Collectors.toList()),self);

        String rightHash = buildRightTree(Arrays.stream(rootDirectory.listFiles())
                .filter(File::isDirectory)
                .map(d->{
                    HashNode node = new HashNode();
                    //node.left = null; node.right=null;
                    node.file = d;
                    node.id = node.file.getName();
                    node.hash = getSHA2HexValue(d.getAbsolutePath()); //TODO - fix this
                    return node;
                }).collect(Collectors.toList()),self);

        self.setHash(getSHA2HexValue(leftHash+ rightHash));

        return self;

    }

    private String buildLeftTree(List<HashNode> files, HashNode parent)
    {

        MerkleTree tree = build1(files);
        tree.root.parent = parent;
        parent.left = tree.root;
        return tree.root.hash;
    }

    private String buildRightTree(List<HashNode> dirs, HashNode parent)
    {
        // for each directory build a sub tree first
        dirs.stream().forEach(dir-> build(dir.file, dir));

        // then build a merkte tree for the directories

        // then assign the parent


        MerkleTree tree = build1(dirs);
        tree.root.parent = parent;
        parent.right = tree.root;
        return tree.root.hash;
    }



    private MerkleTree build1(List<HashNode> nodes) {

        List<HashNode> attribNodes = new ArrayList<>();
        attribNodes.addAll(nodes);

        int diff = diffFromPowerof2(attribNodes);
        for (int i=0;i<diff;i++)
        {
            attribNodes.add(HashNode.Empty());
        }

        MerkleTree tree = new MerkleTree();

        Queue<HashNode>queue = new ArrayDeque();



        /* put AttribNodes in a queue .  then process the queue

        If attribu node - add a LeafHashNode and put in the queue .
        if leaf hashnode , take another leaf has node - create a hashnode and put in the queue
        if hashnode and there is more in the queue , take another hash node and create a hashnode as parent and put in the queue
        if last hashnode - create a blockchain.merkletree and set this node as the root and return the merkle tree so created.
         */

        queue.addAll(attribNodes);
        while(!queue.isEmpty())
        {
            HashNode node1 = queue.remove();
                if (queue.isEmpty()) // root node
                {
                    tree.setRoot(node1);
                    return tree;

                }
                buildHashNode(queue, node1);
        }

        throw new RuntimeException("Error creating tree");

    }

    private void buildHashNode(Queue<HashNode> queue, HashNode node1) {
        HashNode node2 = queue.remove();
        HashNode hashNode = new HashNode();
        hashNode.setId(RandomIdGenerator.getId());
        node1.setParent(hashNode);
        node2.setParent(hashNode);
        hashNode.setLeft(node1);
        hashNode.setRight(node2);
        hashNode.setHash(getSHA2HexValue(node1.getHash()+ node2.getHash()));
        queue.add(hashNode);
        return;
    }

    public static MerkleTree merge(MerkleTree... trees)
    {

        Queue<MerkleTree>queue = new ArrayDeque();

        for (MerkleTree t : trees)
        {
            queue.add(t);
        }

        while (!queue.isEmpty())
        {
            MerkleTree tree1 = queue.remove();
            if (queue.isEmpty())
                return tree1;
            MerkleTree tree2 = queue.remove();

            MerkleTree tree = mergeTwoTrees(tree1,tree2);
            queue.add(tree);
        }

        System.err.println("Should not reach here ");
        return null;

    }




    private static MerkleTree mergeTwoTrees(MerkleTree tree1 , MerkleTree tree2)
    {
        HashNode root1 = tree1.getRoot();
        HashNode root2 = tree2.getRoot();


        HashNode root = new HashNode();
        root1.setParent(root);
        root2.setParent(root);
        root.setLeft(root1);
        root.setRight(root2);
        root.setId(RandomIdGenerator.getId());
        root.setHash(getSHA2HexValue(root1.getHash()+ root2.getHash()));

        MerkleTree tree = new MerkleTree();
        tree.setRoot(root);

        return tree;



    }

    public static MerkleTree copy(MerkleTree src)
    {
        String str = toJSON(src);
        MerkleTree dest = fromJSON(str);
        return dest;
    }

    public static String toJSON(MerkleTree tree)
    {
        try {
            String str = mapper.writeValueAsString(tree);
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String toPrettyPrintJSON(MerkleTree tree)
    {
        try {
            String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }


    //

    public static MerkleTree fromJSON(String str)
    {
        try {
            MerkleTree tree = mapper.readValue(str,MerkleTree.class);
            return tree;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

/*
    public static boolean validateTree(MerkleTree tree)
    {
        // if left and right are not null - then compute the hash of the children and compare to value stored .
        // if not same , then return false , if same then continue checking on both left and right side of the subtree .

        return traverseAndValidateHashInTree(tree.getRoot());


    }


    private static boolean traverseAndValidateHashInTree(Node root)
    {
        if (root==null)
        {
            return true;
        }

        if (root.getLeft()==null && root.getRight()==null) // AttributeNode or LeafHashNode that has its attribute Notshared.
        {
            return true;   // end of tree
        }

        String sha2HexValue;
        if (root.getLeft()!=null && root.getRight()==null) // LeafHashNode
        {
             sha2HexValue = getSHA2HexValue(root.getLeft().getName()+root.getLeft().getValue());

        }
        else
        {
            sha2HexValue = getSHA2HexValue(root.getLeft().getHash() + root.getRight().getHash());

        }
        String hash1 = root.getHash();

        if (hash1.equals(sha2HexValue))
        {
            return true && traverseAndValidateHashInTree(root.getLeft()) && traverseAndValidateHashInTree(root.getRight());
        }
        else
        {
            return false;
        }



    }
*/



    //     String sha2HexValue = getSHA2HexValue(left.tuple.value + right.tuple.value);

  public static String getSHA2HexValue(String str) {
        byte[] cipher_byte;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for(byte b: cipher_byte) {
                sb.append(String.format("%02x", b&0xff) );
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    private int diffFromPowerof2(List<?> attributeNodes)
    {
        int size = attributeNodes.size();
        if (size==0 || size==1)
            return 2-size;  // 2 empty nodes will be added.

        int count = 1;
        while(count<size)
        {
            count = count*2;
        }

        int diff = count-size;
        return diff;


    }



}
