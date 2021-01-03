package markdown;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TOCGenerator {

    public static void main(String[] args) {


        TOCGenerator.generateToc("/home/manoj/data/Maya-1.0.markdown" , "/home/manoj/data/inputtoc.markdown");

    }

    static String[] tabArray = {"","" , "\t" , "\t\t" , "\t\t\t", "\t\t\t\t" , "\t\t\t\t\t" , "\t\t\t\t\t\t"};

    static String[] chars = {"*" , "+" , "-"};

    static int charsCounter=0;

    public static void generateToc(String inputFileName, String outputFileName)
    {

        try {
            try(BufferedReader reader = new BufferedReader(new FileReader(inputFileName)))
            {
                String line = reader.readLine();

                while(line!=null)
                {
                    processHeading(line);
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processHeading(String line)
    {
        if (isHeading(line))
        {
            StringBuilder builder = new StringBuilder();
            int level = headingLevel(line);
            if (level==1)
            {
                builder.append("-");

            }
            else
            {
                int index = charsCounter++%chars.length;
                builder.append(tabArray[level]);
                builder.append(chars[index]);
            }

            line = getRestOfLine(line,level);

            builder.append("[" + line.trim() + "]");
            builder.append("(#");

            String [] words = line.trim().split(" ");
            for (int i=0;i<words.length-1;i++)
            {
                builder.append(words[i] + "_");
            }

            builder.append(words[words.length-1]);

            builder.append(")");

            System.out.println(builder.toString());
        }



    }

    private static String getRestOfLine(String line, int start)
    {
        return line.substring(start);
    }


    private static boolean isHeading(String line)
    {

        return line.length()>0 && line.charAt(0)=='#';


    }

    private static int headingLevel(String line)
    {
        int count=0;
        for (int i=0;i<line.length();i++)
        {
            if (line.charAt(i)=='#')
                count++;
            else
                break;
        }

        return count;
    }
}
