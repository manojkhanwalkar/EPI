package hbase;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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


    public Optional<WALRecord> get(String tableName, String rowId , String colId)
    {
        // sort the colrow bloom filters by date and start checking back from the latest one .
        // once found - read the data file associated with the filter and binary search the file for the row and col id in memory
        // assume no false positives in bloom filter

        String directory = DIR + tableName + "/";

        File dirFile = new File(directory);

        var indexFiles = FileUtils.listFiles(dirFile,new WildcardFileFilter("*.rowcol.filter"),null);

        var reverseSortedIndexFiles = indexFiles.stream().sorted(Comparator.comparingLong(File::lastModified).reversed()).collect(Collectors.toList());

        List<WALRecord> answers = new ArrayList<>() ;

        reverseSortedIndexFiles.stream().forEach(f->{

            if (answers.size()>0)
                return;

            try {
                InputStream inputStream = new FileInputStream(f);

                BloomFilter filter = BloomFilter.readFrom(inputStream,Funnels.stringFunnel(Charsets.US_ASCII));

                if (filter.mightContain(rowId+colId))
                {


                    String[] arr = f.getAbsolutePath().split("\\.");
                    String fileName = arr[0];
                    File dataFile = new File(fileName);

                    List<String> lines = com.google.common.io.Files.readLines( dataFile, Charsets.UTF_8 );

                    var records = lines.stream().filter(line->line.startsWith(rowId+","+colId)).map(line->new WALRecord(tableName,line)).sorted()
                            .collect(Collectors.toList());


                    answers.addAll(records);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (answers.size()>0)
                return Optional.of(answers.get(answers.size()-1));
        else
            return Optional.empty();


    }

    public Optional<List<WALRecord>> get(String tableName, String rowId)
    {
        // sort the colrow bloom filters by date and start checking back from the latest one .
        // once found - read the data file associated with the filter and binary search the file for the row and col id in memory
        // assume no false positives in bloom filter

        String directory = DIR + tableName + "/";

        File dirFile = new File(directory);

        var indexFiles = FileUtils.listFiles(dirFile,new WildcardFileFilter("*.row.filter"),null);

        var sortedIndexFiles = indexFiles.stream().sorted(Comparator.comparingLong(File::lastModified)).collect(Collectors.toList());

        List<WALRecord> answers = new ArrayList<>() ;

        sortedIndexFiles.stream().forEach(f->{


            try {
                InputStream inputStream = new FileInputStream(f);

                BloomFilter filter = BloomFilter.readFrom(inputStream,Funnels.stringFunnel(Charsets.US_ASCII));

                if (filter.mightContain(rowId))
                {


                    String[] arr = f.getAbsolutePath().split("\\.");
                    String fileName = arr[0];
                    File dataFile = new File(fileName);

                    List<String> lines = com.google.common.io.Files.readLines( dataFile, Charsets.UTF_8 );

                    var records = lines.stream().filter(line->line.startsWith(rowId)).map(line->new WALRecord(tableName,line)).sorted()
                            .collect(Collectors.toList());


                    answers.addAll(records);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (answers.size()>0) {

            Set<WALRecord> records = new HashSet<>();
            answers.stream().sorted().forEach(r->{
                if (records.contains(r))
                {
                    records.remove(r);
                }
                records.add(r);

            });


            return Optional.of(new ArrayList<>(records));

        }
        else
            return Optional.empty();


    }





}
