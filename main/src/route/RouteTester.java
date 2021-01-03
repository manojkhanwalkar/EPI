package route;

import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RouteTester {

    public static void main(String[] args)throws Exception  {

        File file = new File("/home/manoj/data/route/routes");
        List<String> lines = Files.readLines(file, Charset.defaultCharset());

        int num = Integer.valueOf(lines.get(0));

        List<Node> nodes = new ArrayList<>(num);

        for (int i=0;i<num;i++)
        {
            nodes.add(new Node(i));
        }

        lines.subList(1,lines.size()).stream().forEach(line->{

            String[] strings = line.split(" ");

            int n1 = Integer.valueOf(strings[0]);

            for (int i=1;i<strings.length;i++)
            {
                int other = Integer.valueOf(strings[i]);

                Edge edge = new Edge(nodes.get(n1),nodes.get(other));
                nodes.get(n1).add(edge);

                edge = new Edge(nodes.get(other), nodes.get(n1));
                nodes.get(other).add(edge);
            }

        });



        for (int i=0;i<nodes.size();i++) {
            nodes.forEach(n -> n.calculateRoutes());
        }


        System.out.println(nodes);

        System.out.println(nodes.get(3).printRoute(7));



    }

}
