package aero;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexRecordWriter {

    String fileName = "/home/manoj/data/aero/indexfile";

    long position;

    RandomAccessFile writer;

    public IndexRecordWriter()
    {
        try {
            writer = new RandomAccessFile(fileName,"rw");
            position = writer.length();
            writer.seek(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized long write(String key, Index.IndexTuple value)
    {
        long curr = position;


        try {
            String str = IndexRecord.serialize(key,value);

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
