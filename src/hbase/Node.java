package hbase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {


    static final int SIZE=10;

    Set<WALRecord> records = new HashSet<>();

    WALManager manager = new WALManager();

    public synchronized void put(String tableName, String rowId , String colId , String colValue)
    {
            WALRecord record = new WALRecord(tableName,rowId,colId,colValue);
            records.add(record);
            if (records.size()==SIZE)
            {
                // persist the records and create a new in memory set .
                manager.persist(records);

                records = new HashSet<>();
            }
    }


    public void delete(String tableName, String rowId, String colId)
    {

    }

    public void delete(String tableName, String rowId)
    {

    }


    public WALRecord get(String tableName, String rowId, String colId)
    {
        return null ;
    }

    public List<WALRecord> get(String tableName, String rowId)
    {
            return null;
    }


}
