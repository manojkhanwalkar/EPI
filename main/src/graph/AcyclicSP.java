package graph;

import java.io.File;
import java.util.PriorityQueue;

import static graph.DirectedEdgeGraph.createSampleGraph;

public class AcyclicSP {



    int[] distTo;

    int[] pathTo ;
    public AcyclicSP(Graph graph, int source)
    {
        pathTo =  new int[graph.getAdjacent().length];
        distTo = new int[graph.getAdjacent().length];

        process(graph,source);
    }


    private void relax(Edge edge)
    {
        if (distTo[edge.to()] > distTo[edge.from()] + edge.getWeight())
        {
            distTo[edge.to()] = distTo[edge.from()] + edge.getWeight();
            pathTo[edge.to()] = edge.from();
        }
    }


     private void process(Graph graph, int source) {


            for (int i=0;i<distTo.length;i++)
                distTo[i] = Integer.MAX_VALUE;

            distTo[source] = 0;

            DFS dfs = new DFS((DirectedEdgeGraph)graph,source);

            dfs.reversePostOrder().forEach(v->{


                for (Edge edge : graph.getAdjacent()[v])
                {
                    relax(edge);
                }
            });











    }



    private static void shortestPath(Graph graph)
    {

        AcyclicSP dsp = new AcyclicSP(graph,0);

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
         shortestPath(graph);

         System.out.println("=============================================");

         graph = graph.negateWeight();
         shortestPath(graph);  // longest path


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
