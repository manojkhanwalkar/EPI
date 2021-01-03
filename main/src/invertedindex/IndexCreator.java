package invertedindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IndexCreator {

    static class StringStringTuple implements Comparable<StringStringTuple>
    {
        String key ;
        String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public StringStringTuple(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return   value ;  
        }

        @Override
        public int compareTo(StringStringTuple other) {

            return this.key.compareTo(other.key);
        }
    }

    public void create(String dirName)
    {
        File dir = new File(dirName);

        var res = Arrays.stream(dir.listFiles()).flatMap(file->{

            List<StringStringTuple> fileContents = new ArrayList();
            try(BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String line = reader.readLine();
                while(line!=null)
                {
                    fileContents.add(new StringStringTuple(line,file.getName()));
                    line = reader.readLine();
                }
            } catch (Exception ex) { ex.printStackTrace(); }

            return fileContents.stream();
        }).flatMap(lineFile->{
            List<StringStringTuple> wordfile = new ArrayList<>();
            String line = lineFile.key;
            Arrays.stream(line.split(" ")).forEach(word-> wordfile.add(new StringStringTuple(word,lineFile.value)));

            return wordfile.stream();
        }).sorted().collect(Collectors.groupingBy(StringStringTuple::getKey));

        System.out.println(res);

    }


    public static void main(String[] args) {


        IndexCreator creator = new IndexCreator();

        creator.create("/home/manoj/data/invertedindex");
    }

}
