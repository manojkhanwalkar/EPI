package lookahead;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class TrieTester {

    public static void main(String[] args) throws Exception {

    //    Map<String,Integer> map = new HashMap<>();

        List<String> words = new ArrayList<>();

        Trie root = new Trie();

        File file = new File("/home/manoj/data/lookahead/words1");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
/*
        while(line!=null)
        {
            Integer count = map.get(line);
            if (count==null)
            {
                map.put(line,1);
            }
            else
            {
                map.put(line, count+1);
            }

            line = bufferedReader.readLine();
        }


        final Comparator<Map.Entry<String, Integer>> valueComparator =
                Map.Entry.comparingByValue(Comparator.reverseOrder());

        List<String> words = map.entrySet().stream().sorted(valueComparator).map(e->e.getKey()).limit(9).collect(Collectors.toList());


        words.stream().forEach(w-> root.insert(w));
*/


        while(line!=null)
        {
            words.add(line);

            line = bufferedReader.readLine();
        }


        final Comparator<Map.Entry<String, Long>> valueComparator =
                Map.Entry.comparingByValue(Comparator.reverseOrder());

        Map<String,Long> map =  words.stream().collect(groupingBy(Function.identity(), Collectors.counting()));
        words = map.entrySet().stream().sorted(valueComparator).map(e->e.getKey()).limit(9).collect(Collectors.toList());

        System.out.println(map);

        words.stream().forEach(w-> root.insert(w));


       System.out.println(Trie.allWordsStartingWith("ta",root));


    }


}
