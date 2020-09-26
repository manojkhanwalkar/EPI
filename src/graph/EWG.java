package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class EWG implements Graph{

    @Override
    public Graph negateWeight() {

        // undirected graph - just deep clone it .

        EWG graph = new EWG(this.vertices);

        List<Edge>[] adj = getAdjacent();
        for (int i=0;i<adj.length;i++)
        {
            List<Edge> edges = adj[i];

            edges.stream().forEach(edge-> {

                WeightedEdge edge1 = new WeightedEdge(edge.from(),edge.to(),-1* edge.getWeight());

                graph.add(edge1);
            });
        }

        return graph;

    }

    @Override
    public Graph reverse() {
        // undirected graph - just deep clone it .

        EWG graph = new EWG(this.vertices);

        List<Edge>[] adj = getAdjacent();
        for (int i=0;i<adj.length;i++)
        {
            List<Edge> edges = adj[i];

            edges.stream().forEach(edge-> {

                WeightedEdge edge1 = new WeightedEdge(edge.from(),edge.to(),edge.getWeight());

                graph.add(edge1);
            });
        }

        return graph;

    }

    int vertices;
    // int edges;


    @Override
    public int getVertices() {
        return vertices;
    }

    List<?>[] adj1;

    List<Edge>[] adj;

    @Override
    public List<Edge>[] getAdjacent() {
        return adj;
    }

    public EWG(int vertices) {
        this.vertices = vertices;
        // this.edges = edges;

        adj1 = new ArrayList<?>[vertices];

        for (int i = 0; i < adj1.length; i++) {
            adj1[i] = new ArrayList<>();
        }

        adj = (List<Edge>[]) adj1;
    }

    public void add(Edge edge) {
        adj[edge.from()].add(edge);
        adj[edge.to()].add(edge);
    }


    public List<Edge> adjacent(int vertex) {
        return adj[vertex];
    }


    public static EWG createSampleGraph(File file) throws Exception {



        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] str = reader.readLine().split(" ");
            EWG graph = new EWG(Integer.valueOf(str[0]));

            String s = reader.readLine();


            while (s != null) {
                str = s.split(" ");
                WeightedEdge edge = new WeightedEdge(str[0], str[1], str[2]);
                graph.add(edge);

                s = reader.readLine();
            }


            return graph;
        }


    }

}
