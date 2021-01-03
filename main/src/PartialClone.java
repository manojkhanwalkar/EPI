import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PartialClone {


    public static void main(String[] args) {

        test1();
        test2();
        test3(); 

    }

    enum Color { BLACK , WHITE , GRAY}

   static  class Vertex {

        Color color = Color.WHITE;

        List<Vertex> edges = new ArrayList<>();

        public void add(Vertex vertex)
        {
            edges.add(vertex);
        }




    }


    public static List<Vertex> clone(List<Vertex> graph, Vertex start)
    {
        // clone a new graph based on all vertices reachable from start.

        List<Vertex> copy = new ArrayList<>();

        Queue<Vertex> vertices = new ArrayDeque<>();
        vertices.add(start);
        while(!vertices.isEmpty())
        {
            Vertex curr = vertices.remove();
            copy.add(curr);
            curr.color=Color.BLACK;
            for (Vertex child : curr.edges)
            {
                if (child.color!=Color.BLACK)
                    vertices.add(child);
            }
        }

        return copy;
    }

    public static void test1()
    {
        Vertex a = new Vertex();
        Vertex b = new Vertex();
        Vertex c = new Vertex();
        Vertex d = new Vertex();

        Vertex e = new Vertex();

        a.add(b);
        b.add(c);
        b.add(d);
        d.add(a);

        List<Vertex> graph = List.of(a,b,c,d,e);

        System.out.println(clone(graph,a));

    }


    public static void test2()
    {
        Vertex a = new Vertex();
        Vertex b = new Vertex();
        Vertex c = new Vertex();
        Vertex d = new Vertex();

        Vertex e = new Vertex();

        a.add(b);
        b.add(c);
        c.add(d);

        List<Vertex> graph = List.of(a,b,c,d,e);

        System.out.println(clone(graph,b));

    }

    public static void test3()
    {
        Vertex a = new Vertex();
        Vertex b = new Vertex();
        Vertex c = new Vertex();
        Vertex d = new Vertex();

        Vertex e = new Vertex();

        a.add(b);
        a.add(c);
        b.add(c);
        c.add(d);
        b.add(d);

        List<Vertex> graph = List.of(a,b,c,d,e);

        System.out.println(clone(graph,c));

    }






}
