package aero;

import java.io.IOException;
import java.io.RandomAccessFile;

import static aero.Index.InvalidLocation;

public class DataRecordReader {

    String fileName;  // = "/home/manoj/data/aero/datafile";



    RandomAccessFile reader;

    public DataRecordReader(String fileName)
    {

        this.fileName = fileName;
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

    public DataRecord readNext()
    {


        return  DataRecord.deserialize(reader);



    }

    public long getCurrentPosition()
    {
        try {
            return reader.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return InvalidLocation;
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
