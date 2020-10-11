package aero;

import java.util.HashMap;
import java.util.Map;

import static aero.Index.InvalidLocation;

public class Node {

    Index index = new Index();


    public void put(String key, String value)
    {
        long position = dataRecordWriter.write(key,value);

       // System.out.println(dataRecordReader.read(position));

        var tuple = index.put(key,position);

        long indexPos = indexRecordWriter.write(key,tuple);

      //   System.out.println(indexRecordReader.read(indexPos));


    }


    public String get(String key)
    {
        long position = index.get(key);
        if (position!=InvalidLocation)
        {
            return dataRecordReader.read(position).value;
        }
        return null;
    }

    public void delete(String key)
    {
        var tuple =  index.delete(key);
        long indexPos = indexRecordWriter.write(key,tuple);
        //System.out.println(indexRecordReader.read(indexPos));


    }


    DataRecordWriter dataRecordWriter = new DataRecordWriter();
    IndexRecordWriter indexRecordWriter = new IndexRecordWriter();

    DataRecordReader dataRecordReader = new DataRecordReader();   //TODO - create a reader pool

    IndexRecordReader indexRecordReader = new IndexRecordReader();  //TODO - to be used in revcovery .

    public void shutdown()
    {
        dataRecordWriter.close();
        indexRecordWriter.close();
    }


    public Node()
    {
        recover();
        System.out.println("Recovered index " + index.index);
    }

    public void recover()
    {
        // only index needs to be recovered .

        index.index=indexRecordReader.recover();
    }




}
