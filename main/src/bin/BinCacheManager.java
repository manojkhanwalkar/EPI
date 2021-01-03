package bin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Optional;

public class BinCacheManager {


    volatile BinCache binCache = null;
    public BinCache get()
    {
        return binCache;
    }

    public  void create(String fileName) throws Exception
    {

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

            BinCache binCache = new BinCache();

            String line = bufferedReader.readLine();
            while(line!=null)
            {

                processLine(line, binCache);

                line = bufferedReader.readLine();
            }
          //  String sample = "057164;PRIVATE LABEL CARD;;DEBIT;PIN ONLY W/O EBT;UNITED STATES;US;USA;840;;";

            if(this.binCache!=null) {

                this.binCache.refresh(binCache.cache);

            }
            else
            {
                this.binCache = binCache;
            }

        }
    }


    private  void processLine(String line, BinCache cache)
    {
        String[] fields = line.split(";");

        BinRecord binRecord = BinRecord.create(fields);

        cache.cache.put(binRecord.getBin(),binRecord);
    }


    public static void main(String[] args) throws Exception {

        BinCacheManager cacheManager = new BinCacheManager();


        DirectoryWatcherExample.watch(cacheManager);


    }


     static class DirectoryWatcherExample {

        public static void watch(BinCacheManager binCacheManager) throws Exception  {
            WatchService watchService
                    = FileSystems.getDefault().newWatchService();

            Path path = Paths.get("/home/manoj/data/bindata/");

            Files.list(path).sorted().forEach(System.out::println);



            Optional<Path> lastFilePath = Files.list(path)    // here we get the stream with full directory listing
                    .filter(f -> !Files.isDirectory(f))  // exclude subdirectories from listing
                    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));  // finally get the last file using simple comparator by lastModified field

            if ( lastFilePath.isPresent() ) // your folder may be empty
            {
                // do your code here, lastFilePath contains all you need
                binCacheManager.create(lastFilePath.get().toString());

                System.out.println(binCacheManager.binCache);
            }

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY
                    );

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                   binCacheManager.create(path.toString()+"/"+event.context());

                   System.out.println(binCacheManager.binCache);

                }
                key.reset();
            }
        }
    }

}
