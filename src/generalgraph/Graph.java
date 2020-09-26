package generalgraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Graph {

    Map<String,Vertex> vertices = new HashMap<>();

    public void addVertex(Vertex vertex)
    {

        vertices.put(vertex.id,vertex);
    }

    public void addEdge(Edge edge)
    {
          Vertex from = edge.from;
          from.addEdge(edge);
    }


    public Collection<Vertex> getVertices()
    {
        return vertices.values();
    }

    public List<Vertex> getVertices(String type)
    {
        return vertices.values().stream().filter(v->v.type.equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    public Vertex getVertex(String id)
    {
        return vertices.get(id);
    }



    @Override
    public String toString() {
        return "Graph{" +
                "vertices=" + vertices +
                '}';
    }
}
