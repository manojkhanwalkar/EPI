package graph;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class DFS {


    // prints the vertices while processing the garph in dfs

    DirectedEdgeGraph graph;
    int[] marked;


    Queue<Integer> pre = new ArrayDeque<>();
    Queue<Integer> post = new ArrayDeque<>();
    Queue<Integer> reversePost = new ArrayDeque<>();

    Stack<Integer> tmp = new Stack<>();


    public Iterable<Integer> reversePostOrder()
    {
        return  reversePost;
    }



    public DFS(DirectedEdgeGraph graph, int vertex)
    {

        this.graph=graph;

        marked  = new int[graph.vertices];

        dfs(vertex);

        while(!tmp.isEmpty())
        {
            reversePost.add(tmp.pop());
        }

    }







    private void dfs(int vertex)
    {
      //  System.out.println(vertex);
        pre.add(vertex);
        marked[vertex]=1;

        for (Edge edge : graph.adj[vertex])
        {
            if (marked[edge.to()]==0)
            {
                dfs(edge.to());
            }
        }

        post.add(vertex);
        tmp.push(vertex);
    }


    public static void main(String[] args) throws Exception {

        File file = new File("/home/manoj/IdeaProjects/EPI/src/resources/topology.txt");

        DirectedEdgeGraph graph = DirectedEdgeGraph.createSampleGraph(file);
        DFS dfs = new DFS(graph,0);



    }
}
