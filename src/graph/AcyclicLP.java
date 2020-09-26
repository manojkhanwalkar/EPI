package graph;

import java.io.File;

import static graph.DirectedEdgeGraph.createSampleGraph;

public class AcyclicLP {



    int[] distTo;

    int[] pathTo ;
    public AcyclicLP(Graph graph, int source)
    {
        pathTo =  new int[graph.getAdjacent().length];
        distTo = new int[graph.getAdjacent().length];

        process(graph,source);
    }


    private void relax(Edge edge)
    {
        if (distTo[edge.to()] < distTo[edge.from()] + edge.getWeight())
        {
            distTo[edge.to()] = distTo[edge.from()] + edge.getWeight();
            pathTo[edge.to()] = edge.from();
        }
    }


     private void process(Graph graph, int source) {


            for (int i=0;i<distTo.length;i++)
                distTo[i] = Integer.MIN_VALUE;

            distTo[source] = 0;

            DFS dfs = new DFS((DirectedEdgeGraph)graph,source);

            dfs.reversePostOrder().forEach(v->{

                System.out.println(v);


                for (Edge edge : graph.getAdjacent()[v])
                {
                    relax(edge);
                }
            });






    




    }



    public static void longestPath(Graph graph, int source)
    {

        AcyclicLP dsp = new AcyclicLP(graph,source);

        int[] distTo = dsp.getSP();

        for (int i=0;i<distTo.length;i++)
        {
            System.out.println(i + " ---> " + distTo[i]);
        }


        dsp.printPath(0,6);

    }



    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/dag.txt");
         Graph graph = createSampleGraph(file);
         longestPath(graph,0);


        }







    public void printPath(int source, int tgt)
    {
        int start = tgt;

        System.out.println(start);
        while(true)
        {
            start = pathTo[start];

            System.out.println(start);

            if (start==source)
                break;
        }
    }

    private int[] getSP() {

        return distTo;
    }

}
