package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


// Uses a Matrix instead of the adjacency list .

public   class DirectedEdgeGraph1 implements Graph{

    @Override
    public Graph negateWeight() {

        int[][] adj1 = new int[this.vertices][this.vertices];

        for (int i=0;i<this.vertices;i++)
        {
            for (int j=0;j<this.vertices;j++)
            {
                adj1[i][j]= adj[i][j] * -1 ;
            }
        }

        DirectedEdgeGraph1 graph = new DirectedEdgeGraph1(this.vertices,adj1);

        return graph;

    }

    @Override
    public Graph reverse() {


        int[][] adj1 = new int[this.vertices][this.vertices];

        for (int i=0;i<this.vertices;i++)
        {
            for (int j=0;j<this.vertices;j++)
            {
                adj1[i][j]= this.adj[j][i];
            }
        }

        DirectedEdgeGraph1 graph = new DirectedEdgeGraph1(this.vertices,adj1);

        return graph;

    }

    int vertices;


    @Override
    public int getVertices() {
        return vertices;
    }



    int [][] adj;


    private void initialize()
    {
        for (int i=0;i<vertices;i++)
        {
            for (int j=0;j<vertices;j++)
            {
                adj[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public DirectedEdgeGraph1(int vertices) {
        this.vertices = vertices;

        adj = new int[vertices][vertices];

        initialize();

    }


    private DirectedEdgeGraph1(int vertices, int[][] adj) {
        this.vertices = vertices;

        this.adj = adj;

    }

    public void add(Edge edge) {
        adj[edge.from()][edge.to()] = edge.getWeight();

    }


    public int[] adjacent(int vertex) {
        return adj[vertex];
    }


    public static DirectedEdgeGraph1 createSampleGraph(File file) throws Exception {


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String vertices = reader.readLine();
            DirectedEdgeGraph1 graph = new DirectedEdgeGraph1(Integer.valueOf(vertices));


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


    public static DirectedEdgeGraph1 createSampleGraphForCriticalPath(File file) throws Exception {


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            int vertices = Integer.valueOf(reader.readLine());

            DirectedEdgeGraph1 graph = new DirectedEdgeGraph1(2*vertices+2);


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