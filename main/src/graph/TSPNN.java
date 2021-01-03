package graph;

// Travelling salesman using nearest neighbour

import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class TSPNN {

    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/tst.txt");


        EWG graph = EWG.createSampleGraph(file);


        TSPNN tspnn = new TSPNN(graph);

        int length = tspnn.process();

        System.out.println("Tour length is " + length);

    }


    Graph graph ;
    public TSPNN(Graph graph) {

        this.graph = graph;
    }


    public int process()
    {

        int[] marked = new int[graph.getVertices()];

        int count=0;

        Random random = new Random();

        int current= random.nextInt(marked.length-1);

        for (int i=0;i<graph.getVertices();i++)
        {
            marked[current] =1;

            for (Edge edge : graph.getAdjacent()[i])  {

                if (marked[edge.to()]==0) {
                    current = edge.to();
                    count=count+edge.getWeight();
                    break;
                }
            }
        }

        return count;
    }
}
