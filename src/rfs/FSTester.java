package rfs;

import com.amazonaws.services.opsworks.model.App;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FSTester {

    private static List<String> getFileContents(String name)
    {

        List<String>  contents = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(name)))
        {

            String line = reader.readLine();
            while(line!=null)
            {
                contents.add(line);

                line = reader.readLine();
            }

        } catch (Exception e) { e.printStackTrace(); }

        return contents;

    }

    public static void main(String[] args) throws Exception {


        FileSystem fileSystem = new FileSystem();

        fileSystem.compress();

        System.out.println("======================================================");

        fileSystem.printDirs();

        fileSystem.printFiles();

        System.out.println("======================================================");



      /*  CompletableFuture<List<String>> f1 = CompletableFuture.supplyAsync(()-> { return fileSystem.read("/word1") ; });
        CompletableFuture<List<String>> f2 = CompletableFuture.supplyAsync(()-> { return fileSystem.read("/home/data/word3") ; });
        CompletableFuture<List<String>> f3 = CompletableFuture.supplyAsync(()-> { return fileSystem.read("/home/word2") ; });

        CompletableFuture.allOf(f1,f2,f3).join();

        f1.get().forEach(System.out::println);
           System.out.println("======================================================");

        f2.get().forEach(System.out::println);
        System.out.println("======================================================");

        f3.get().forEach(System.out::println);
        System.out.println("======================================================");*/

      try(RFSFileReader fileReader = FileSystem.getReader("/word2", fileSystem)) {

          Iterator<String> iter = fileReader.iterator();
          while (iter.hasNext()) {
              String s = iter.next();

              System.out.println(s);
          }

      }


        Runtime.getRuntime().addShutdownHook(new Thread(new INodeWriter(fileSystem)));


    /*  fileSystem.createFile("/word1");
        fileSystem.write("/word1",getFileContents("/home/manoj/data/words1.txt"), FileSystem.WriteMode.OverWrite);
*/

        fileSystem.deleteFile("/word2");

       try(RFSFileWriter fileWriter = FileSystem.getWriter("/word2",fileSystem))
       {
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);

       }



        fileSystem.createDir("/home/");

        fileSystem.createDir("/home/data/");
        fileSystem.createFile("/home/data/word3");


        fileSystem.createDir("/home/test/");

        fileSystem.createDir("/test/");

        fileSystem.printDirs();


        fileSystem.createFile("/home/word2");
        fileSystem.createFile("/test/word1");
        fileSystem.createFile("/home/test/word1");

      //  fileSystem.read("/home/data/word3").forEach(System.out::println);

        CompletableFuture wf1 = CompletableFuture.runAsync(()-> { fileSystem.write("/word1",getFileContents("/home/manoj/data/words1.txt"), FileSystem.WriteMode.OverWrite); });
        CompletableFuture wf2 = CompletableFuture.runAsync(()->{fileSystem.write("/home/word2",getFileContents("/home/manoj/data/words2.txt"), FileSystem.WriteMode.OverWrite); });

        CompletableFuture wf3 = CompletableFuture.runAsync(()-> { fileSystem.write("/home/data/word3",getFileContents("/home/manoj/data/words3.txt"), FileSystem.WriteMode.Append); });

        CompletableFuture.allOf(wf1,wf2,wf3).join();

        fileSystem.write("/home/data/word3",getFileContents("/home/manoj/data/words3.txt"), FileSystem.WriteMode.OverWrite);

      fileSystem.printFiles();


        //fileSystem.read("/home/word2").forEach(System.out::println);

        fileSystem.deleteFile("/home/data/word3");

       fileSystem.printFiles();

        fileSystem.deleteDirAndUnderlying("/home/");

       System.out.println("======================================================");

       fileSystem.printDirs();



// "org/apache/tika/mime/custom-mimetypes.xml"


    }
}
