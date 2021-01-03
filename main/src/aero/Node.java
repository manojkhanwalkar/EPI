package aero;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import static aero.Index.InvalidLocation;

public class Node {

    Index index = new Index();

    private ObjectPool<DataRecordReader> pool;


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
            try {
                DataRecordReader dataRecordReader = pool.borrowObject();
                String str =  dataRecordReader.read(position).value;

                pool.returnObject(dataRecordReader);

                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delete(String key)
    {
        var tuple =  index.delete(key);
        long indexPos = indexRecordWriter.write(key,tuple);
        //System.out.println(indexRecordReader.read(indexPos));


    }


    DataRecordWriter dataRecordWriter = new DataRecordWriter("/home/manoj/data/aero/datafile");
    IndexRecordWriter indexRecordWriter = new IndexRecordWriter("/home/manoj/data/aero/indexfile");

  //  DataRecordReader dataRecordReader = new DataRecordReader();   //TODO - create a reader pool

    IndexRecordReader indexRecordReader = new IndexRecordReader("/home/manoj/data/aero/indexfile");  //TODO - to be used in revcovery .

    public void shutdown()
    {
        dataRecordWriter.close();
        indexRecordWriter.close();
    }


    public Node()
    {
        recover();
        System.out.println("Recovered index " + index.index);

        pool = new GenericObjectPool<>(new ReaderFactory());


//ReaderUtil readerUtil = new ReaderUtil(new GenericObjectPool<StringBuffer>(new StringBufferFactory()));
    }

    public void recover()
    {
        // only index needs to be recovered .

        index.index=indexRecordReader.recover();
    }




}
