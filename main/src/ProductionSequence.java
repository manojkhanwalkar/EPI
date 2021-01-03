import java.util.*;
import java.util.stream.Collectors;

public class ProductionSequence {

    static class Vertex {

        public Vertex(String name) {
            this.name = name;
            this.path = name;
        }

        String name;

        List<Vertex> edges = new ArrayList<>();

        public void add(Vertex vertex)
        {
            edges.add(vertex);
        }

        String path;

        public void setPath(String s)
        {
            path = s + " " + name;
        }

    }

    static String[] strings = {"cat","cot","dot","pot","pat","pam","mat","TTT","pet","jet","set","ram"};

    public static void main(String[] args) {

        test1();

        test2();

        test3();

    }


    public static void test1()
    {
        Map<String,Vertex> vertices = makeGraph(strings);

        System.out.println(isPath("cat","dot",vertices));

    }

    public static void test2()
    {
        Map<String,Vertex> vertices = makeGraph(strings);

        System.out.println(isPath("cat","TTT",vertices));
    }


    public static void test3()
    {
        Map<String,Vertex> vertices = makeGraph(strings);

        System.out.println(isPath("pet","ram",vertices));
    }




    public static boolean isPath(String start,String end,Map<String,Vertex> vertices)
    {

        Set<Vertex> visited = new HashSet<>();

        Queue<Vertex> queue = new ArrayDeque<>();
        queue.add(vertices.get(start) );



        while(!queue.isEmpty())
        {
            Vertex curr = queue.remove();
            visited.add(curr);



            for (Vertex child : curr.edges)
            {

                if (child.name.equalsIgnoreCase(end)) {
                    System.out.println(curr.path + "  " + child.name);
                    return true;
                }
                if (!visited.contains(child))
                {
                    child.setPath(curr.path);
                    queue.add(child);
                    visited.add(child);
                }
            }
        }

        return false;
    }

    public static Map<String,Vertex> makeGraph(String... elements)
    {

        List<Vertex> vertices = new ArrayList<>();
        for (String s: elements)
        {
            vertices.add(new Vertex(s));
        }
        for (int i=0;i<elements.length-1;i++)
        {
            for (int j=i+1;j<elements.length;j++)
            {
                if (distance(elements[i],elements[j])==1)
                {
                    vertices.get(i).add(vertices.get(j));
                    vertices.get(j).add(vertices.get(i));
                }
            }
        }

        Map<String,Vertex> vertexMap = new HashMap<>();
        vertices.stream().forEach(vertex-> vertexMap.put(vertex.name,vertex));

        return vertexMap;

    }


    public static int distance(String s1, String s2)
    {
        if (s1.length()!=s2.length())
            throw new RuntimeException("Strings are of different lenghts");

        int len = s1.length();
        int diff = 0;

        for (int i=0;i<len;i++)
        {
            if (s1.charAt(i)!=s2.charAt(i))
                diff++;
        }

        return diff;
    }



}
