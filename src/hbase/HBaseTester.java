package hbase;

import java.util.List;
import java.util.Optional;

public class HBaseTester {

    public static void main(String[] args) {


        Node node = new Node();

      //  String str = "/home/manoj/data/hbase/T1/3b283882-dbd8-4339-8924-5f5ed887e867.rowcol.filter";

        //String[] arr = str.split("\\.");

    /*    for (int i=0;i<25;i++)
        {
            node.put("T1" , "R1" , "C"+i , "Hello World Again ");
        }

        node.delete("T1" , "R1" , "C1" );*/


        Optional<WALRecord> record = node.get("T1", "R1", "C5");

        System.out.println(record);


        record = node.get("T1", "R1", "C1");

        System.out.println(record);

        Optional<List<WALRecord>> records = node.get("T1","R1");

        System.out.println(records);


        node.delete("T1" , "R1");





    }




}
