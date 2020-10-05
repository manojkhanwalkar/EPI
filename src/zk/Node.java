package zk;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Node implements Serializable {

    public long serialversionUID = 1l;

    public enum Type { Ephemeral , Peristent } ;

    String name ;  // fully qualified path

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    transient Node parent ;
    transient Set<Node> children = new HashSet<>();

    Map<String,String> attributes;

    int version;

    Type type ;

    public Node()
    {

    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", version=" + version +
                ", type=" + type +
                '}';
    }


    public static Node copy(Node other)
    {
        Node node = new Node();
        node.name = other.name;
        node.type = other.type;

        return node;
    }
}
