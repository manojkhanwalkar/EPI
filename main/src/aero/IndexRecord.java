package aero;


import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexRecord {

    static char EOR = (char)30;
    static char EOF = (char)31;

    final String key;

    final Index.IndexTuple tuple;

    public IndexRecord(String key, Index.IndexTuple tuple) {
        this.key = key;
        this.tuple = tuple;
    }

    public static String serialize(String key, Index.IndexTuple tuple)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        builder.append(EOF);
        builder.append(tuple.location);
        builder.append(EOF);
        builder.append(tuple.marker);
        builder.append(EOF);
       // builder.append(EOR);

        return builder.toString();
    }


    public static IndexRecord deserialize(String record)
    {
        int keyStart=0;
        int keyEnd=0;
        int locStart=0;
        int locEnd=0;
        int markStart=0;
        int markEnd=0;

       for (int i=0;i<record.length();i++)
        {
                if (record.charAt(i)==EOF)
                {
                    keyEnd = i-1;
                    break;
                }
        }

       locStart = keyEnd+2;

        for (int i=keyEnd+2;i<record.length();i++)
        {
            if (record.charAt(i)==EOF)
            {
                locEnd = i-1;
                break;
            }

        }

        markStart = locEnd+2;

        for (int i=markStart;i<record.length();i++)
        {
            if (record.charAt(i)==EOF)
            {
                markEnd = i-1;
                break;
            }

        }


        String key = record.substring(keyStart,keyEnd+1);

        long location = Long.valueOf(record.substring(locStart,locEnd+1));

        boolean marker =  Boolean.valueOf(record.substring(markStart,markEnd+1));




        return new IndexRecord(key,new Index.IndexTuple(location,marker));




    }


    public static IndexRecord deserialize(RandomAccessFile reader)  throws IOException
    {
        int keyStart=0;
        int keyEnd=0;
        int locStart=0;
        int locEnd=0;
        int markStart=0;
        int markEnd=0;

        StringBuilder builder = new StringBuilder();

        int i=0;
        while(true) {
            char c = reader.readChar();
            builder.append(c);
            if (c == EOF) {
                keyEnd = i - 1;
                break;
            }

            i++;

        }

        locStart = keyEnd+2;

        i=locStart;

        while(true) {

            char c = reader.readChar();
                builder.append(c);
            if (c == EOF) {
                locEnd = i - 1;
                break;
            }

            i++;
        }

        markStart = locEnd+2;

        i=markStart;

        while(true) {

            char c = 0;
                c = reader.readChar();
                builder.append(c);
            if (c == EOF) {
                markEnd = i - 1;
                break;
            }

            i++;
        }



       String record = builder.toString();

        String key = record.substring(keyStart,keyEnd+1);

        long location = Long.valueOf(record.substring(locStart,locEnd+1));

        boolean marker =  Boolean.valueOf(record.substring(markStart,markEnd+1));


        return new IndexRecord(key,new Index.IndexTuple(location,marker));



    }


    @Override
    public String toString() {
        return "IndexRecord{" +
                "key='" + key + '\'' +
                ", tuple=" + tuple +
                '}';
    }

    public static void main(String[] args) {


        String str = serialize("Hello" , new Index.IndexTuple(1000, true));

        System.out.println(str);

        IndexRecord dataRecord = IndexRecord.deserialize(str);

        System.out.println(dataRecord);

    }


}
