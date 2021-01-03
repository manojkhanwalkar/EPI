package graph;

import java.io.File;

public class SCC {


    /* Kosaraju -

    1. reverse the graph ,
    2. get the reverse post order on the reverse graph ,
    3. do CC dfs search using the order in 2 for the vertices.
     */


    Graph graph;

    int count;

    int id[] ;

    int[] marked;

    public SCC(Graph graph)
    {
        this.graph=graph;

        Graph rev = graph.reverse();

        DFS dfs = new DFS((DirectedEdgeGraph)rev,0);

        var iter = dfs.reversePostOrder();

        iter.forEach(i -> {
            id = new int[graph.getVertices()];
            marked = new int[graph.getVertices()];
            if (marked[i]==0)
            {
                dfs(i);
                count++;
            }


        });

    }


    private void dfs(int vertex)
    {
        marked[vertex] = 1;
        id[vertex] = count;

        for (Edge edge : graph.getAdjacent()[vertex])
        {

            int other = edge.from()==vertex ? edge.to() : edge.from();
            if (marked[other]==0)
            {
                dfs(other);
            }
        }


    }


    public int number()
    {
        return count;
    }

    public boolean connected(int u, int v)
    {
        return id[u]==id[v];
    }

    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/scc.txt");
        DirectedEdgeGraph graph = DirectedEdgeGraph.createSampleGraph(file);

        SCC cc = new SCC(graph);

        System.out.println(cc.number());



    }

}
