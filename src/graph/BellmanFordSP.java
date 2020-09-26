package graph;

import java.io.File;
import java.util.Arrays;
import java.util.PriorityQueue;

import static graph.DirectedEdgeGraph.createSampleGraph;

public class BellmanFordSP {







    int[] distTo;

    int[] pathTo ;
    public BellmanFordSP(Graph graph, int source)
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

            for (int i=0;i<graph.getVertices();i++)
            {
                for (int j=0;j<graph.getVertices();j++)
                {
                    for (Edge edge : graph.getAdjacent()[j])
                    {
                        relax(edge);
                    }
                }
            }




    }

    private boolean hasNegativeCycle(Graph graph)
    {

        int oldDistTo[] = Arrays.copyOf(distTo,distTo.length);

        for (int j=0;j<graph.getVertices();j++)
        {
            for (Edge edge : graph.getAdjacent()[j])
            {
                relax(edge);
            }
        }

        int compare = Arrays.compare(oldDistTo, 0, oldDistTo.length, distTo, 0, distTo.length);

        if (compare==0)
            return false;
        else
            return true;

    }



    private static void shortestPath(Graph graph)
    {

        BellmanFordSP dsp = new BellmanFordSP(graph,0);

        if (!dsp.hasNegativeCycle(graph)) {


            int[] distTo = dsp.getSP();

            for (int i = 0; i < distTo.length; i++) {
                System.out.println(i + " ---> " + distTo[i]);
            }


            dsp.printPath(0, 6);

        }
        else {


            System.out.println("Has a negative cycle");

        }

    }



    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/dag.txt");
         Graph graph = createSampleGraph(file);
         shortestPath(graph);

      /*   System.out.println("=============================================");

         graph = graph.negateWeight();
         shortestPath(graph);  // longest path
*/

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
