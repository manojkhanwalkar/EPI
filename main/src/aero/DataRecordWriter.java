package aero;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DataRecordWriter {

    String fileName ; //= "/home/manoj/data/aero/datafile";

    long position;

    RandomAccessFile writer;

    public DataRecordWriter(String fileName)
    {
        this.fileName = fileName;
        try {
            writer = new RandomAccessFile(fileName,"rw");
            position = writer.length();
            writer.seek(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized long write(String key,String value)
    {
        long curr = position;


        try {
            String str = DataRecord.serialize(key,value);

            writer.writeChars(str);

            position = writer.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return curr;

    }


    public synchronized void close()
    {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
