package graph;

import java.io.File;
import java.util.Arrays;

import static graph.DirectedEdgeGraph1.createSampleGraph;

public class FloydWarshall {







    int[][] distTo;

    int[][] pathTo ;
    public FloydWarshall(Graph graph, int source)
    {
        pathTo =  new int[graph.getVertices()][graph.getVertices()];
        distTo = new int[graph.getVertices()][graph.getVertices()];

        process((DirectedEdgeGraph1)graph,source);
    }


    private void relax(int k , int i, int j)
    {
        if (distTo[i][k]==Integer.MAX_VALUE || distTo[k][j] == Integer.MAX_VALUE)
            return;

        if (distTo[i][j] > distTo[i][k] + distTo[k][j])
        {
            distTo[i][j] = distTo[i][k] + distTo[k][j];
           // pathTo[edge.to()] = edge.from();
        }
    }


     private void process(DirectedEdgeGraph1 graph, int source) {



        for (int i=0;i<graph.getVertices();i++)
        {
            for (int j=0;j<graph.getVertices();j++) {


                distTo[i][j] = graph.adj[i][j];

            }
        }


           for (int k=0;k<graph.getVertices();k++) {

               for (int i = 0; i < graph.getVertices(); i++) {
                   for (int j = 0; j < graph.getVertices(); j++) {

                       relax(k,i,j);

                     /*  for (Word edge : graph.getAdjacent()[j]) {
                           relax(edge);
                       }*/
                   }
               }
           }




    }

 /*   private boolean hasNegativeCycle(Graph graph)
    {

        int oldDistTo[] = Arrays.copyOf(distTo,distTo.length);

        for (int j=0;j<graph.getVertices();j++)
        {
            for (Word edge : graph.getAdjacent()[j])
            {
                relax(edge);
            }
        }

        int compare = Arrays.compare(oldDistTo, 0, oldDistTo.length, distTo, 0, distTo.length);

        if (compare==0)
            return false;
        else
            return true;

    }*/



    private static void shortestPath(Graph graph)
    {

        FloydWarshall dsp = new FloydWarshall(graph,0);

     //   if (!dsp.hasNegativeCycle(graph)) {


            int[][] distTo = dsp.getSP();

            for (int i=0;i<distTo.length;i++)
            {
                for (int j=0;j<distTo.length;j++)
                {
                    if (distTo[i][j]!=Integer.MAX_VALUE)  // only print paths that exist .
                        System.out.println(i + " ===>" + j  + "======" + distTo[i][j]);
                }
            }


          //  dsp.printPath(0, 6);

 /*       }
        else {


            System.out.println("Has a negative cycle");

        }*/

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







  /*  public void printPath(int source, int tgt)
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
    }*/

    private int[][] getSP() {

        return distTo;
    }

}
