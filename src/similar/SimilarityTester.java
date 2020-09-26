package similar;

import javax.print.attribute.HashAttributeSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimilarityTester {

    public static void main(String[] args) {

        SimilarityTester tester = new SimilarityTester();

        tester.test();


    }

    public void test()
    {
        String dir = "/home/manoj/data/similar/";

        isSimilar(dir+"a", dir+"b");

        isSimilarPara(dir+"a", dir+"b");

    }


    private static final Pattern UNWANTED_SYMBOLS =
            Pattern.compile("[^a-zA-Z ]| ");

    private String preprocess(String line)
    {
        Matcher unwantedMatcher = UNWANTED_SYMBOLS.matcher(line);
       line = unwantedMatcher.replaceAll("").toLowerCase();

      //  return line.replaceAll("[^a-zA-Z ]| ", "").toLowerCase();

       return line;

    }

    private String concatenate(List<String> lines)
    {
        StringBuilder builder = new StringBuilder();
        lines.stream().forEach(line->builder.append(line));

        return builder.toString();

    }

    private int hash(String string)
    {
        return string.hashCode();
    }

    private boolean isSimilar(String str1, String str2)
    {

        File file1 = new File(str1);

        File file2 = new File(str2);

        Set<Integer> lineHashes = new HashSet();

        int similarLines =0;

        int fileOneTotal=0;

        int fileTwoTotal=0;

        try(BufferedReader reader = new BufferedReader(new FileReader(file1)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
                line = preprocess(line);
                lineHashes.add(hash(line));
                fileOneTotal++;
                line = reader.readLine();
            }

        } catch (Exception e) {

                e.printStackTrace();

    }

        try(BufferedReader reader = new BufferedReader(new FileReader(file2)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
                line = preprocess(line);

                int hash = hash(line);
                if (lineHashes.contains(hash))
                {
                    similarLines++;
                }
                fileTwoTotal++;
                line = reader.readLine();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }


        System.out.println("Similar lines " + similarLines);

        int totalLines = (fileOneTotal>fileOneTotal? fileOneTotal: fileTwoTotal);

        int percentSimilar = 100*(similarLines/totalLines);

        System.out.println("% similar " + percentSimilar);

        return percentSimilar>50;




    }



    static int PARA_SIZE = 5;
    private boolean isSimilarPara(String str1, String str2)
    {

        File file1 = new File(str1);

        File file2 = new File(str2);

        Set<Integer> lineHashes = new HashSet();

        int similarParas =0;

        int fileOneTotal=0;

        int fileTwoTotal=0;

        try(BufferedReader reader = new BufferedReader(new FileReader(file1)))
        {
            List<String> para = new ArrayList<>();
            String line = reader.readLine();
            int count=1;
            while(line!=null)
            {
                line = preprocess(line);
                if (count<PARA_SIZE)
                {
                    para.add(line);
                }
                else {
                    para.add(line);
                    String str = concatenate(para);
                    lineHashes.add(hash(str));
                    para.clear();
                    count=0;
                }
                fileOneTotal++;
                line = reader.readLine();
                count++;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file2)))
        {
            List<String> para = new ArrayList<>();
            int count=1;
            String line = reader.readLine();
            while(line!=null)
            {
                line = preprocess(line);
                if (count<PARA_SIZE)
                {
                    para.add(line);
                }
                else
                {
                    para.add(line);
                    String str = concatenate(para);
                    int hash = hash(str);
                    if (lineHashes.contains(hash))
                    {
                        similarParas++;
                    }

                    para.remove(0);

                }

                fileTwoTotal++;
                line = reader.readLine();
                count++;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }


        System.out.println("Similar paras " + similarParas);

        int totalLines = (fileOneTotal>fileOneTotal? fileOneTotal: fileTwoTotal);

        int percentSimilar = 100*(PARA_SIZE*similarParas/totalLines);

        System.out.println("% similar " + percentSimilar);

        return percentSimilar>50;




    }



}
