package aero;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class IndexRecordReader {

    String fileName ; // = "/home/manoj/data/aero/indexfile";



    RandomAccessFile reader;

    public IndexRecordReader(String fileName)
    {
        this.fileName = fileName;
        try {
            reader = new RandomAccessFile(fileName,"r");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IndexRecord read(long start)
    {


        try {
            reader.seek(start);
            return  IndexRecord.deserialize(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;





    }



    public   Map<String, Index.IndexTuple> recover()
    {
        Map<String, Index.IndexTuple> index = new HashMap<>();

        try {
            reader.seek(0);
            while(reader.getFilePointer()!=reader.length()) {

                IndexRecord indexRecord = IndexRecord.deserialize(reader);

                index.put(indexRecord.key,indexRecord.tuple);
            }



        } catch (IOException e) {
           // e.printStackTrace();
        }

        return index;




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
