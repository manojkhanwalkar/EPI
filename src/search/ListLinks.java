package search;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {


    static String parseText(Document doc)
    {


        String text = doc.body().text(); // "An example link"
     //   System.out.println(text);

        return text;


    }

    static Set<String> tokenize(String text)
    {
        // replace special chars with space
        text = Pattern.compile("[^a-zA-Z0-9]").matcher(text).replaceAll(" ");

     //   System.out.println(text);

        // start with spaces


        text = text.trim().replaceAll("  +"," ");

       // System.out.println(text);

        // ignore if contains numbers
  //      text = text.replaceAll("[0-9]","");


        //System.out.println(text);

         String[] textTokens = text.split(" ");

        text.matches(".*\\d.*");

         return Arrays.stream(textTokens).filter(t->!t.matches(".*\\d.*")).collect(Collectors.toSet());






    }

    static PriorityQueue<String> queue = new PriorityQueue<>(10, Comparator.comparingInt(String::length));

    static void processTop10(String str)
    {
        if (queue.size()<10)
        {
            queue.add(str);
        }
        else
        {
            String other = queue.peek();
            if (str.length() > other.length())
            {
                queue.remove();
                queue.add(str);
            }

        }
    }

    public static void main(String[] args) throws IOException {


        Queue<String> process = new ArrayDeque<>();

        String url = "http://news.ycombinator.com/";

        process.add(url);

        int count=0;
        while (!process.isEmpty() && count <100)
        {
            String curr = process.remove();
            if (uniqueAndValidDomain(curr))
            {
                try {
                    Document doc = Jsoup.connect(curr).get();
                    Elements links = doc.select("a[href]");
                   // String str = parseText(doc);

                   // Set<String> tokens = tokenize(str);

                   // tokens.stream().map(t->t.toLowerCase()).collect(Collectors.toSet()).forEach(ListLinks::processTop10);
                    for (Element link : links) {
                        process.add(link.attr("abs:href"));
                     }

                    count++;
                } catch (IOException e) {
                   // e.printStackTrace();
                    domains.remove(curr);
                }

            }


        }

        System.out.println(domains.keySet());

        while(!queue.isEmpty())
        {
            System.out.println(queue.remove());
        }



    }


    static Map<String,String> domains = new HashMap<>();

    private static boolean uniqueAndValidDomain(String link)
    {
        boolean result = false;

        if (link.startsWith("http://") || link.startsWith("https://")) {
            int start = link.indexOf("/") + 2;
            int end = link.indexOf("/", start + 1);

            String str = link.substring(start, (end < start) ? link.length() : end);

            //  System.out.println(link + "   " + str);

            if (!domains.containsKey(str)) {
                domains.put(str, link);
                return true;
            }

        }

        return result;
    }
}
