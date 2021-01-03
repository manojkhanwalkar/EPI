package graph;

import java.io.File;

import static graph.AcyclicLP.longestPath;
import static graph.DirectedEdgeGraph.createSampleGraph;
import static graph.DirectedEdgeGraph.createSampleGraphForCriticalPath;

public class CriticalPathScheduling {



    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/criticalpath.txt");
         Graph graph = createSampleGraphForCriticalPath(file);
         longestPath(graph,20);


        }






}
