package fs.merkletree;

import org.junit.jupiter.api.Test;

import java.io.File;

public class MerkleTreeTester {

    @Test
    public void createTree()
    {


        MerkleTreeUtil merkleTreeUtil = new MerkleTreeUtil();
        var tree = merkleTreeUtil.build(new File("/home/manoj/data/"));

        // fsr.replicate("/home/manoj/data","/home/manoj/replicatetest");

      //  System.out.println(MerkleTreeUtil.toPrettyPrintJSON(tree));

        //String treeStr = MerkleTreeUtil.toJSON(tree);

        var tree1 = merkleTreeUtil.build(new File("/home/manoj/replicatetest/"));


        System.out.println("Tree is valid " + MerkleTreeUtil.validateTree(tree));

        System.out.println("Tree is valid " + MerkleTreeUtil.validateTree(tree1));


        System.out.println("Tree is same " + MerkleTreeUtil.isSame(tree,tree1));
    }


}
