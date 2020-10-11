package bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileDB {


    static class FileIndexTuple
    {


        BloomFilter<CharSequence> bloomfilter ;
        List<String> lines = new ArrayList<>();

        public FileIndexTuple(String fileName)
        {
            if (Files.exists(Paths.get(fileName+".bloom")))
            {
                try {
                    bloomfilter = BloomFilter.readFrom(new FileInputStream(fileName+".bloom"),Funnels.stringFunnel(Charsets.US_ASCII) );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {

                bloomfilter = BloomFilter.create(Funnels.stringFunnel(Charsets.US_ASCII), 1000000, 0.001);
            }


            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName)))
            {

                String line ;
                while ((line= bufferedReader.readLine())!=null)
                {
                    bloomfilter.put(line);
                    lines.add(line);

                }



            } catch (Exception ex) { ex.printStackTrace(); }


            Collections.sort(lines);

            try(FileOutputStream stream = new FileOutputStream(fileName+".bloom"))
            {
                bloomfilter.writeTo(stream);


            }catch (Exception ex) { ex.printStackTrace(); }
        }

        public String search(String key)
        {
            int start = 0; int end = lines.size()-1;

            while (start<=end)
            {

                int mid = start + (end-start) /2;
                String line = lines.get(mid);

                int res = key.compareTo(line);

                if (res==0)
                    return line;

                if (res<0)
                {
                    // key is smaller
                    end = mid;
                    continue;
                }

                if (res>0)
                {
                    start = mid;
                    continue;
                }



            }

            return null;
        }


    }


    List<FileIndexTuple> tuples = new ArrayList<>();

    public void add(String fileName)
    {
        FileIndexTuple tuple = new FileIndexTuple(fileName);

        tuples.add(tuple);

    }


    public boolean contains(String key)
    {
        Optional<FileIndexTuple> result = tuples.stream().filter(tuple-> tuple.bloomfilter.mightContain(key)).findAny();

        if (result.isPresent())
            return true;
        else
            return false;
    }

    public Optional<String> search(String key)
    {
        Optional<FileIndexTuple> result = tuples.stream().filter(tuple-> tuple.bloomfilter.mightContain(key)).findAny();
        if (result.isPresent())
        {
            return Optional.of(result.get().search(key));
        }
        else
        {
            return Optional.empty();
        }

    }


}
