package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static graph.DirectedEdgeGraph.createSampleGraph;

public class DSP {




    int[] marked;


    int[] distTo;

    int[] pathTo ;
    public DSP(Graph graph, int source)
    {
        pathTo =  new int[graph.getAdjacent().length];
        distTo = new int[graph.getAdjacent().length];
        marked = new int[graph.getAdjacent().length];

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
            marked[0] = 1;

        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();

        for (Edge edge : graph.getAdjacent()[0]) {

            priorityQueue.add(edge);
        }


        while(!priorityQueue.isEmpty())
        {

            Edge edge = priorityQueue.remove();

            relax(edge);


            int u = edge.to();

                marked[u]=1;

                for (Edge edge1 : graph.getAdjacent()[u]) {
                    if (marked[edge1.to()]==1)
                        continue;
                    else
                        priorityQueue.add(edge1);
                }



        }




    }



    private static void shortestPath(Graph graph)
    {

        DSP dsp = new DSP(graph,0);

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
