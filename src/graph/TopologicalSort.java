package graph;

import java.io.File;
import java.util.Stack;

public class TopologicalSort {



        public static void main(String[] args) throws Exception {

            File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/topology.txt");

            DirectedEdgeGraph graph = DirectedEdgeGraph.createSampleGraph(file);
            DFS dfs = new DFS(graph,0);

            Iterable<Integer> iter = dfs.reversePostOrder();

           iter.forEach(i->System.out.println(i));

           System.out.println("=========================");




        }



}
