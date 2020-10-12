package hbase;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

public class WALManager {

    static final String DIR =  "/home/manoj/data/hbase/";


    public void persist(Set<WALRecord> records )
    {
        // sort by table , then row and column

        // create unique file name

        // write all sorted records for that table in that file

        // create two bloom filters for that file - one by rowid and one by rowid and column id


        // for each table - create a dir by that name if it does not exist.
        records.stream().forEach(record->{

            String tableName = record.tableName;
            if (!Files.exists(Paths.get(DIR+tableName)))
            {
                try {
                    Files.createDirectory(Paths.get(DIR+tableName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        });

        //Map<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
        //  .collect(groupingBy(BlogPost::getType));

        var records1 = records.stream().sorted().collect(groupingBy(WALRecord::getTableName));

        records1.entrySet().stream().forEach(entry->{

            BloomFilter<CharSequence> rowIDFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.US_ASCII), 100, 0.001);
            BloomFilter<CharSequence> rowColIDFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.US_ASCII), 100, 0.001);

            File file = new File(DIR+entry.getKey()+"/" + UUID.randomUUID().toString() + "/");
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))
            {
                entry.getValue().stream().forEach(record->{

                    try {

                        bufferedWriter.write(record.rowId + "," + record.colId + "," + record.colValue + "," + record.timeStamp + "," + record.marker);

                        bufferedWriter.newLine();

                        rowIDFilter.put(record.rowId);

                        rowColIDFilter.put(record.rowId + record.colId);

                    }catch (Exception ex) {
                            ex.printStackTrace(); }

                });

            }   catch (Exception ex) {
        ex.printStackTrace(); }


        try {
                String path = file.getAbsolutePath();

                String rowFilterFileName  = path + ".row" + ".filter";

                String rowColFilterFileName  = path + ".rowcol" + ".filter";


                FileOutputStream rowFilterFile = new FileOutputStream(rowFilterFileName);

                rowIDFilter.writeTo(rowFilterFile);

                FileOutputStream rowColFilterFile = new FileOutputStream(rowColFilterFileName);

                rowColIDFilter.writeTo(rowColFilterFile);
            } catch (IOException e) {
                e.printStackTrace();
            }


        });




    }

}
