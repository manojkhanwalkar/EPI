package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MST {


    List<Edge> mst = new ArrayList<>();

    public MST(EWG graph) {

            int[] marked = new int[graph.adj.length];

            marked[0] = 1;

        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();

        for (Edge edge : graph.adjacent(0)) {

            priorityQueue.add(edge);
        }


        while(!priorityQueue.isEmpty())
        {
            // take edge and if second vertex is not marked then mark that vertex and take all that vertices edges which point to not marked vertices.

            Edge edge = priorityQueue.remove();
            int u = edge.from();
            if (marked[u]==1)
            {
                u=edge.to();
            }
            if (marked[u]==0) // superfluous check
            {
                marked[u]=1;

                for (Edge edge1 : graph.adjacent(u)) {
                    if (marked[edge1.from()]==1 && marked[edge1.to()]==1)
                        continue;
                    else
                        priorityQueue.add(edge1);
                }

                mst.add(edge);


            }
        }

    }


    public List<Edge> getMst()
    {
        return mst;
    }

    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/ewg.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String[] str = reader.readLine().split(" ");
            EWG graph = new EWG(Integer.valueOf(str[0]));

            String s = reader.readLine();


            while (s!=null)
            {
                str = s.split(" ");
                WeightedEdge edge = new WeightedEdge(str[0],str[1],str[2]);
                graph.add(edge);

                s=reader.readLine();
            }


            MST mst = new MST(graph);

            System.out.println(mst.getMst());
        }






    }

}
