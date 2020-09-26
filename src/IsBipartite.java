import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class IsBipartite {


    public static void main(String[] args) {

        test1();
        test2();
       // test3();

    }

    enum Color { Green, Blue, UnMarked}

   static  class Vertex {

        boolean processed = false;

        Color color = Color.UnMarked;

        List<Vertex> edges = new ArrayList<>();

        public void add(Vertex vertex)
        {
            edges.add(vertex);
        }




    }

    private static Color getNextColor(Color color)
    {
        return color==Color.Blue? Color.Green : Color.Blue;
    }


   public static boolean isBiPartite( Vertex start)
   {

       start.color=Color.Blue;
      // start.processed=true;

       Queue<Vertex> queue = new ArrayDeque<>();
       queue.add(start);
       while(!queue.isEmpty())
       {
           Vertex current = queue.remove();
           current.processed=true;

           Color color = getNextColor(current.color);
           for (Vertex child : current.edges)
           {
               if (child.processed)
               {
                   if (child.color!=color)
                       return false;
                   else
                       continue;
               }
               else if (child.color!=Color.UnMarked)
               {
                   if (child.color!=color)
                       return false;
               }

               child.color=color;
               queue.add(child);

           }
       }

       return true;
    }

    public static void test1()
    {
        Vertex a = new Vertex();
        Vertex b = new Vertex();
        Vertex c = new Vertex();
        Vertex d = new Vertex();

        Vertex e = new Vertex();

        a.add(b);
        a.add(c);
        b.add(d);
        c.add(e);



        System.out.println(isBiPartite(a));

    }


    public static void test2()
    {
        Vertex a = new Vertex();
        Vertex b = new Vertex();
        Vertex c = new Vertex();
        Vertex d = new Vertex();

        Vertex e = new Vertex();

        a.add(b); a.add(c);
        b.add(c);
        c.add(e);
        b.add(d);


        System.out.println(isBiPartite(a));

    }








}
