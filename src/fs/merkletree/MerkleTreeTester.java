package fs.merkletree;

import org.junit.jupiter.api.Test;

import java.io.File;

public class MerkleTreeTester {

    @Test
    public void createTree()
    {


        MerkleTreeUtil merkleTreeUtil = new MerkleTreeUtil();
        var tree = merkleTreeUtil.build(new File("/home/manoj/data/merkletree"));

        System.out.println(MerkleTreeUtil.toPrettyPrintJSON(tree));



    }


}
