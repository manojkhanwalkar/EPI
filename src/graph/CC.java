package graph;

import java.io.File;

public class CC {


    Graph graph;

    int count;

    int id[] ;

    int[] marked;

    public CC(Graph graph)
    {
        this.graph=graph;
        id = new int[graph.getVertices()];
        marked = new int[graph.getVertices()];

        for (int i=0;i<graph.getVertices();i++)
        {
            if (marked[i]==0)
            {
                dfs(i);
                count++;
            }
        }
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

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/cc.txt");
        EWG graph = EWG.createSampleGraph(file);

        CC cc = new CC(graph);

        System.out.println(cc.number());



    }

}
