package aero;


import java.io.IOException;
import java.io.RandomAccessFile;

public class DataRecord {

    static char EOR = (char)30;
    static char EOF = (char)31;

    final String key;
    final String value;
    private DataRecord(String key,String value)
    {
        this.key = key;
        this.value=value;
    }


    public static String serialize(String key, String value)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        builder.append(EOF);
        builder.append(value);
        builder.append(EOF);
        //builder.append(EOR);

        return builder.toString();
    }


    public static DataRecord deserialize(String record)
    {
        int keyStart=0;
        int keyEnd=0;
        int valStart=0;
        int valEnd=0;

        for (int i=0;i<record.length();i++)
        {
                if (record.charAt(i)==EOF)
                {
                    keyEnd = i-1;
                    break;
                }
        }
        valStart = keyEnd+2;

        for (int i=keyEnd+2;i<record.length();i++)
        {
            if (record.charAt(i)==EOF)
            {
                valEnd = i-1;
                break;
            }

        }

        String key = record.substring(keyStart,keyEnd+1);
        String value = record.substring(valStart,valEnd+1);

        return new DataRecord(key,value);


    }


    public static DataRecord deserialize(RandomAccessFile reader)
    {
        int keyStart=0;
        int keyEnd=0;
        int valStart=0;
        int valEnd=0;

        StringBuilder builder = new StringBuilder();

        int i=0;
        while(true) {

            char c = 0;
            try {
                c = reader.readChar();
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(c);


                if (c == EOF) {
                    keyEnd = i - 1;
                    break;
                }

                i++;
        }


            valStart = keyEnd + 2;

        i=valStart;

        while(true) {

            char c = 0;
            try {
                c = reader.readChar();
                builder.append(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (c == EOF) {
                    valEnd = i - 1;
                    break;
                }

                i++;
        }

        String record = builder.toString();

            String key = record.substring(keyStart, keyEnd + 1);
            String value = record.substring(valStart, valEnd + 1);

            return new DataRecord(key, value);




    }


    @Override
    public String toString() {
        return "DataRecord{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static void main(String[] args) {


        String str = serialize("Hello" , "World");

        System.out.println(str);

        DataRecord dataRecord = DataRecord.deserialize(str);

        System.out.println(dataRecord);

    }


}
