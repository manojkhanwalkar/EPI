package userpair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Matcher {


    public static void main(String[] args) throws Exception {

        //fixedAttributeTester();

        largeAndSparseAttributeTester();

    }



    static String dir = "/home/manoj/data/userpair/";
    public static void fixedAttributeTester() throws Exception
    {
        Map<BitSet,Integer> users = new HashMap<>();
        String name = "fixed";
        File file = new File(dir+name);
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
        {
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                String[] strs = line.split(" ");
                int id = Integer.valueOf(strs[0]);
                BitSet attrs = new BitSet(5);
                for (int i=1;i< strs.length;i++)
                {
                    if (strs[i].equals("1"))
                        attrs.set(i);

                }

                Integer existingId =users.get(attrs);
                if (existingId!=null)
                {
                    System.out.println("Match between users " + existingId.intValue() + " " + id );
                    users.remove(attrs);
                }
                else {
                    users.put(attrs, id);
                }

                line = bufferedReader.readLine();
            }

        }

        System.out.println("Unmatched users " + users);
    }


    public static void largeAndSparseAttributeTester() throws Exception
    {
        Map<String,String> users = new HashMap<>();
        String name = "large";
        File file = new File(dir+name);
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file)))
        {
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                String[] strs = line.split(" ");
                String id = strs[0];

                //.reduce(0, (subtotal, element) -> subtotal + element);
                String key = Arrays.stream(strs).filter(s->s.startsWith("A")).sorted().reduce("",(s,t)->s+t);



                String existingId =users.get(key);
                if (existingId!=null)
                {
                    System.out.println("Match between users " + existingId + " " + id );
                    users.remove(key);
                }
                else {
                    users.put(key, id);
                }

                line = bufferedReader.readLine();
            }

        }

        System.out.println("Unmatched users " + users);
    }


}
