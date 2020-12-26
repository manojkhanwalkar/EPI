package fs.merkletree;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import util.RandomIdGenerator;

import java.io.File;

import static fs.merkletree.MerkleTreeUtil.getSHA2HexValue;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class HashNode  {

    public static HashNode Empty()
    {
        HashNode EMPTY = new HashNode();
        EMPTY.left = null ;
        EMPTY.right = null;
        EMPTY.file = null;

        EMPTY.id = RandomIdGenerator.getId();
        EMPTY.hash = getSHA2HexValue("EMPTY NODE");
        return EMPTY;
    }

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  //  @JsonManagedReference
    HashNode parent;
String hash;

//@JsonBackReference
HashNode left ;

//@JsonBackReference
HashNode right;

    File file;



    public HashNode getLeft() {
        return left;
    }


    public HashNode getRight() {
        return right;
    }


    public String getHash() {
        return hash;
    }


    public HashNode getParent() {
        return parent;
    }


    public void setLeft(HashNode node) {
        left = node;
    }


    public void setRight(HashNode node) {
            right = node;
    }


    public void setHash(String hash) {
        this.hash = hash;
    }


    public void setParent(HashNode parent) {
        this.parent = (HashNode)parent;
    }


    public String toString() {
        return "HashNode{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
