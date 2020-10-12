package hbase;

import java.util.Objects;

public class WALRecord implements Comparable<WALRecord> {

    protected final String tableName;
    protected final String rowId;
    protected final String colId;
    protected final String colValue;
    protected final long timeStamp;
    protected  boolean marker = false;

    public WALRecord(String tableName , String line)
    {
        String[] fields = line.split(",");
        this.tableName = tableName;
        this.rowId = fields[0];
        this.colId = fields[1];
        this.colValue = fields[2];

        this.timeStamp = Long.valueOf(fields[3]);

        this.marker = Boolean.valueOf(fields[4]);

    }


    public WALRecord(String tableName, String rowId, String colId, String colValue) {
        this.tableName = tableName;
        this.rowId = rowId;
        this.colId = colId;
        this.colValue = colValue;

        this.timeStamp = System.nanoTime();

    }

    public WALRecord(String tableName, String rowId, String colId, boolean b) {

        this.tableName = tableName;
        this.rowId = rowId;
        this.colId = colId;
        this.timeStamp = System.nanoTime();
        this.marker = true;
        colValue = "";
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WALRecord walRecord = (WALRecord) o;
        return tableName.equals(walRecord.tableName) &&
                rowId.equals(walRecord.rowId) &&
                colId.equals(walRecord.colId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, rowId, colId);
    }


    @Override
    public int compareTo(WALRecord walRecord) {

        int res = tableName.compareTo(walRecord.tableName);
        if (res!=0)
            return res;

        res = rowId.compareTo(walRecord.rowId);
        if (res!=0)
            return res;

        res = colId.compareTo(walRecord.colId);
        if (res!=0)
            return res;

        return Long.compare(timeStamp,walRecord.timeStamp);


    }

    @Override
    public String toString() {
        return "WALRecord{" +
                "tableName='" + tableName + '\'' +
                ", rowId='" + rowId + '\'' +
                ", colId='" + colId + '\'' +
                ", colValue='" + colValue + '\'' +
                ", timeStamp=" + timeStamp +
                ", marker=" + marker +
                '}';
    }
}
