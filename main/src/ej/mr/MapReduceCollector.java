package ej.mr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class MapReduceCollector<T,V> implements collector<T,V> {

    BufferedWriter bufferedWriter ;

    String fileName;

    public MapReduceCollector()
    {
         fileName = "/tmp/" + UUID.randomUUID().toString();

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void collect(pair p) {

        try {
            bufferedWriter.write(p.getT()+ " " + p.getV());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void complete() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
