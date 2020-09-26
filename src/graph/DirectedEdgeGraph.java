package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public   class DirectedEdgeGraph implements Graph{

    @Override
    public Graph negateWeight() {

        DirectedEdgeGraph graph = new DirectedEdgeGraph(this.vertices);

        List<Edge>[] adj = getAdjacent();

        for (int i=0;i<adj.length;i++)
        {
            List<Edge> edges = adj[i];

            edges.stream().forEach(edge->{

                DirectedEdge edge1 = new DirectedEdge(edge.from(), edge.to(), -1*edge.getWeight()); // reverse the direction

                graph.add(edge1);

            });
        }

        return graph;

    }

    @Override
    public Graph reverse() {

        DirectedEdgeGraph graph = new DirectedEdgeGraph(this.vertices);

        List<Edge>[] adj = getAdjacent();

        for (int i=0;i<adj.length;i++)
        {
            List<Edge> edges = adj[i];

            edges.stream().forEach(edge->{

                DirectedEdge edge1 = new DirectedEdge(edge.to(), edge.from(), edge.getWeight()); // reverse the direction

                graph.add(edge1);

            });
        }

        return graph;

    }

    int vertices;


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

    public DirectedEdgeGraph(int vertices) {
        this.vertices = vertices;


        adj1 = new ArrayList<?>[vertices];

        for (int i = 0; i < adj1.length; i++) {
            adj1[i] = new ArrayList<>();
        }

        adj = (List<Edge>[]) adj1;
    }

    public void add(Edge edge) {
        adj[edge.from()].add(edge);

    }


    public List<Edge> adjacent(int vertex) {
        return adj[vertex];
    }


    public static DirectedEdgeGraph createSampleGraph(File file) throws Exception {


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String vertices = reader.readLine();
            DirectedEdgeGraph graph = new DirectedEdgeGraph(Integer.valueOf(vertices));


            String s = reader.readLine();


            while (s != null) {
                String[] str = s.split(" ");
                DirectedEdge edge = new DirectedEdge(str[0], str[1], str[2]);
                graph.add(edge);

                s = reader.readLine();
            }

            return graph;

        }


    }


    public static DirectedEdgeGraph createSampleGraphForCriticalPath(File file) throws Exception {


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            int vertices = Integer.valueOf(reader.readLine());

            DirectedEdgeGraph graph = new DirectedEdgeGraph(2*vertices+2);


            int vertex=0;
            String s = reader.readLine();

            int source = 2*vertices;
            int sink = 2*vertices+1;


            while (s != null) {

                String[] str = s.split(" ");

                int weight = Integer.valueOf(str[0]);

                DirectedEdge edge = new DirectedEdge(vertex, vertex+vertices, weight); // start to end of task.
                graph.add(edge);

                // source to start
                edge = new DirectedEdge(source, vertex, 0);
                graph.add(edge);

                // end to sink
                edge = new DirectedEdge(vertex+vertices, sink, 0);
                graph.add(edge);




                // end to successors

                for (int j=1;j<str.length;j++)
                {
                    int other = Integer.valueOf(str[j]);
                    edge = new DirectedEdge(vertex+vertices, other, 0);
                    graph.add(edge);

                }

               ;

                s = reader.readLine();
                vertex++;
            }

            return graph;

        }


    }

}