package ej;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static void main(String[] args) throws Exception  {

     // sort(args[0]);

//        wc(args[0]);

   //     grep(args[0], "^[0-9][0-9,a-z,A-Z ]*");

 //       find("/home/manoj/data/","[a-z,A-Z,.]*.data");

        merge("/home/manoj/data/words1.txt","/home/manoj/data/words2.txt","/home/manoj/data/words3.txt");

    }


    public static void sort(String fileName) throws Exception

    {
        List<String> lines = readFile(fileName);

        lines.stream().sorted().forEach(System.out::println);
    }

    public static void wc(String fileName) throws Exception

    {
        List<String> lines = readFile(fileName);

        long words = lines.stream().flatMap(l-> Arrays.stream(l.split(" "))).count();

        long numLines = lines.size();

        System.out.println("Number of lines " + numLines);

        System.out.println("Number of words " + words);


    }


    public static void grep(String fileName, String regex) throws Exception

    {
        List<String> lines = readFile(fileName);


        Pattern pattern = Pattern.compile(regex);

        lines.stream().filter(line-> {

            Matcher matcher = pattern.matcher(line);

            return matcher.matches();
        }).forEach(System.out::println);


    }


    public static void merge(String... fileNames) throws Exception

    {

        String fileName = fileNames[0];

        List<String> lines = readFile(fileName);

        for (int i=1;i<fileNames.length;i++)
        {
            lines.addAll(readFile(fileNames[i]));

            lines.sort(Comparator.naturalOrder());


        }


        lines.stream().forEach(System.out::println);


    }





    public static void find(String startDir, String regex) throws Exception

    {

        Pattern pattern = Pattern.compile(regex);

        List<String> fileNames = new ArrayList<>();

        Queue<File> fileQueue = new ArrayDeque<>();

        File root = new File(startDir);

        fileQueue.add(root);

        while(!fileQueue.isEmpty())
        {

            File file = fileQueue.remove();

            if (file.isFile())
            {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches())
                {
                    fileNames.add(file.getName());
                }
            }
            else
            {
                if (file.isDirectory())
                {

                    if (file.listFiles()!=null)
                        fileQueue.addAll(List.of(file.listFiles()));
                }
            }
        }



      fileNames.stream().forEach(System.out::println);

    }


    private static List<String> readFile(String fileName) throws Exception
    {
        File file = new File(fileName);

        List<String> lines = Files.readLines( file, Charsets.UTF_8 );

        return lines;

    }

}
