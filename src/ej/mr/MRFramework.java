package ej.mr;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MRFramework {

    public void submit(MRJob job) throws Exception
    {

        MapReduceCollector mapCollector = new MapReduceCollector();

        MapReduceCollector reduceCollector = new MapReduceCollector();

        Class clazz = Class.forName(job.mapperName);

        Mapper mapper = (Mapper) (clazz.getConstructors()[0].newInstance());


         clazz = Class.forName(job.reducerName);

        Reducer reducer = (Reducer) (clazz.getConstructors()[0].newInstance());



        for (String inputFile : job.inputFiles) {
            map(mapper, mapCollector, inputFile);
        }

        mapCollector.complete();

        String fileToShuffleName = mapCollector.getFileName();

        String outputFileName = "/tmp/" + UUID.randomUUID().toString();

        shuffle(fileToShuffleName,outputFileName);

        reduce(reducer,reduceCollector,outputFileName);

        reduceCollector.complete();


        File file = new File(reduceCollector.getFileName());

        try {
            List<String> lines = Files.readLines( file, Charsets.UTF_8 );

            lines.stream().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void map(Mapper mapper, collector collector, String fileName)
    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName))))
        {
            String line = bufferedReader.readLine();

            while(line!=null)
            {
                mapper.map(line,collector);

                line = bufferedReader.readLine();
            }
        }catch(Exception e) {e.printStackTrace();}

    }

    void shuffle(String inputfileName, String outputFileName)
    {
        List<String> lines =null;

       try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputfileName))))
        {
            lines = bufferedReader.lines().sorted().collect(Collectors.toList());
        } catch (Exception e) { e.printStackTrace(); }


        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter (new File(outputFileName))))

        {

            String prevToken=null;
            List<String> secondTokens = new ArrayList<>();

            for (int i=0;i<lines.size();i++)
            {
            String line = lines.get(i);


                String[] tokens = line.split(" ",2);
                String token1 = tokens[0];
                String token2 = tokens[1];

                if (prevToken == null) {
                    prevToken = token1;
                } else if (prevToken.equals(token1)) {
                } else {
                    shuffleWrite(prevToken, secondTokens, bufferedWriter);
                    // write token1 and list of second tokens to file
                    prevToken = token1;
                    secondTokens.clear();
                }
                secondTokens.add(token2);



            }


        }catch(Exception e) {e.printStackTrace();}

    }

    private void shuffleWrite(String token1, List<String> secondTokens, BufferedWriter bufferedWriter) {

        try {
            bufferedWriter.write(token1);
            secondTokens.stream().limit(secondTokens.size()-1).forEach(t->{

                try {
                    bufferedWriter.write(" " + t + " ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bufferedWriter.write(" " + secondTokens.get(secondTokens.size()-1));

            bufferedWriter.newLine();

            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    void reduce(Reducer reducer, collector collector, String fileName)
    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName))))
        {
            String line = bufferedReader.readLine();

            while(line!=null)
            {
                String[] str = line.split(" ",2);

                List<String> values = new ArrayList<>();

                for (int i=1;i<str.length;i++)
                {
                    values.add(str[i]);

                }

                reducer.reduce(str[0],values, collector);

                line = bufferedReader.readLine();
            }
        }catch(Exception e) {e.printStackTrace();}

    }

}
