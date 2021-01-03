package generalgraph;



import java.util.*;

public class Vertex {

    String type ;

    Map<String,String> attributes = new HashMap<>();

    String id;

    Map<String, List<Edge>>  edgeTypes = new HashMap<>();

    public Vertex(String type,String id)
    {
        this.type=type;
        this.id=id;
    }

    public void addAttribute(String attributeType ,String arributeValue)
    {
        attributes.put(attributeType, arributeValue);
    }


    public void addEdge(Edge edge)
    {
        String edgeType=edge.type;
       List<Edge> edges =  edgeTypes.computeIfAbsent(edgeType, et-> {return new ArrayList<>(); } );
       edges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return type.equals(vertex.type) &&
                id.equals(vertex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }


    @Override
    public String toString() {
        return "Vertex{" +
                "type='" + type + '\'' +
                ", attributes=" + attributes +
                ", id='" + id + '\'' +
                ", edgeTypes=" + edgeTypes +
                '}';
    }
}
