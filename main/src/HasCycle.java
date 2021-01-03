import java.util.ArrayList;
import java.util.List;

public class HasCycle {


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

    public static boolean hasCycle(List<Vertex> graph)
    {
       for (Vertex vertex : graph)
       {
           if (hasCycle(vertex))
               return true;
       }

       return false;
    }

    private static boolean hasCycle(Vertex vertex)
    {
        vertex.color= Color.GRAY;
        for (Vertex next : vertex.edges)
        {
            if (next.color==Color.GRAY)
                return true;
            else if (next.color==Color.WHITE && hasCycle(next))
            {
                return true;
            }
        }

        vertex.color = Color.BLACK;
        return false;
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

        System.out.println(hasCycle(graph));

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

        System.out.println(hasCycle(graph));

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

        System.out.println(hasCycle(graph));

    }






}
