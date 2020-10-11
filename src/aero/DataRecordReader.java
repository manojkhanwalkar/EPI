package aero;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DataRecordReader {

    String fileName = "/home/manoj/data/aero/datafile";



    RandomAccessFile reader;

    public DataRecordReader()
    {
        try {
            reader = new RandomAccessFile(fileName,"r");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataRecord read(long start)
    {


        try {
            reader.seek(start);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  DataRecord.deserialize(reader);



    }


    public void close()
    {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
