package generalgraph;

import java.util.HashMap;
import java.util.Map;

public class Edge {

    public void addAttributes(String attributeType, String attributeValue)
    {
        attributes.put(attributeType,attributeValue);

    }

    Vertex to;


    Map<String,String> attributes = new HashMap<>();

    public Edge(String type, Vertex from, Vertex to)
    {
        this.type = type;
        this.to = to;
        this.from = from;
    }

    Vertex from;


    String type;


    @Override
    public String toString() {
        return "Edge{" +
                "type='" + type + '\'' +
                ", to=" + to.id +
                ", from=" + from.id +
                ", attributes=" + attributes +
                '}';
    }
}
