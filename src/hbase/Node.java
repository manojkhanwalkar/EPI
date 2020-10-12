package hbase;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        WALRecord record = new WALRecord(tableName,rowId,colId,true);
        records.add(record);
        if (records.size()==SIZE)
        {
            // persist the records and create a new in memory set .
            manager.persist(records);

            records = new HashSet<>();
        }

    }

    public void delete(String tableName, String rowId)
    {

    }


    // assume getting from file - ignore records in memory.
    public Optional<WALRecord> get(String tableName, String rowId, String colId)
    {

        return manager.get(tableName,rowId,colId);
    }

    public Optional<List<WALRecord>> get(String tableName, String rowId)
    {
            return manager.get(tableName,rowId);
    }


    public Node()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownProcessor(this)));
    }


}
