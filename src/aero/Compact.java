package aero;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Compact {

    public static void main(String[] args) throws Exception {

        IndexRecordReader indexRecordReader = new IndexRecordReader("/home/manoj/data/aero/indexfile");  //TODO - to be used in revcovery .

        var index = indexRecordReader.recover();

        DataRecordReader dataRecordReader = new DataRecordReader("/home/manoj/data/aero/datafile");

        DataRecordWriter dataRecordWriter = new DataRecordWriter("/home/manoj/data/aero/tmpdatafile");

        while(true)
        {
            long position = dataRecordReader.getCurrentPosition();

            if (position==dataRecordReader.reader.length())
                break;
            DataRecord dataRecord = dataRecordReader.readNext();

            if (index.containsKey(dataRecord.key))
            {
                var tuple = index.get(dataRecord.key);

                if (tuple.marker)  // data has been deleted
                    continue;

                if (tuple.location!=position)  // older record , should not be copied.
                    continue;

                // copy to new data file , get location and update tuple

                long dataLocation = dataRecordWriter.write(dataRecord.key,dataRecord.value);

                tuple.setLocation(dataLocation);

            }


        }

        IndexRecordWriter indexRecordWriter = new IndexRecordWriter("/home/manoj/data/aero/tmpindexfile");

        index.entrySet().stream().filter(entry->entry.getValue().marker==false).forEach(entry -> {

            indexRecordWriter.write(entry.getKey(),entry.getValue());
        });


        indexRecordWriter.close();

        dataRecordWriter.close();

        indexRecordReader.close();

        dataRecordReader.close();

        // copy the files to original name

        Files.delete(Paths.get("/home/manoj/data/aero/indexfile"));
        Files.delete(Paths.get("/home/manoj/data/aero/datafile"));


        Files.move(Paths.get("/home/manoj/data/aero/tmpindexfile"),Paths.get("/home/manoj/data/aero/indexfile"));
        Files.move(Paths.get("/home/manoj/data/aero/tmpdatafile"),Paths.get("/home/manoj/data/aero/datafile"));


    }


}
