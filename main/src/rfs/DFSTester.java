package rfs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DFSTester {

    private static List<String> getFileContents(String name)
    {

    return FSTester.getFileContents(name);
    }

    public static void main(String[] args) throws Exception {


        FileSystem fileSystem1 = new FileSystem("fs1");
        FileSystem fileSystem2 = new FileSystem("fs2");
        fileSystem1.compress();
        fileSystem2.compress();


        DFS dfs = new DFS();

        dfs.register(fileSystem1);
        dfs.register(fileSystem2);


        Runtime.getRuntime().addShutdownHook(new Thread(new DFSNodeWriter(dfs)));

        System.out.println("======================================================");
        dfs.printDirs();
        System.out.println("======================================================");

        dfs.createDir("/home/");

        dfs.createDir("/home/data/");
        dfs.createDir("/home/test/");

        dfs.createDir("/test/");

        dfs.createFile("/word1");

        dfs.createFile("/home/word2");
        dfs.createFile("/test/word1");
        dfs.createFile("/home/test/word1");

        dfs.printDirs();

        System.out.println("======================================================");
        dfs.printFiles();
        System.out.println("======================================================");


        dfs.deleteFile("/test/word1");
        dfs.printFiles();
        System.out.println("======================================================");

        dfs.deleteDirAndUnderlying("/home/");

        dfs.printDirs();

        System.out.println("======================================================");

        dfs.write("/word1",getFileContents("/home/manoj/data/words1.txt"));

        dfs.read("/word1").forEach(System.out::println);



   /*

        fileSystem.printDirs();

        fileSystem.printFiles();

        System.out.println("======================================================");*/



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

/*      try(RFSFileReader fileReader = FileSystem.getReader("/word2", fileSystem)) {

          Iterator<String> iter = fileReader.iterator();
          while (iter.hasNext()) {
              String s = iter.next();

              System.out.println(s);
          }

      }*/





    /*
        fileSystem.write("/word1",getFileContents("/home/manoj/data/words1.txt"), FileSystem.WriteMode.OverWrite);
*/

   /*     fileSystem.deleteFile("/word2");

       try(RFSFileWriter fileWriter = FileSystem.getWriter("/word2",fileSystem))
       {
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);
           getFileContents("/home/manoj/data/words1.txt").forEach(fileWriter::write);

       }




        fileSystem.createFile("/home/data/word3");






      //  fileSystem.read("/home/data/word3").forEach(System.out::println);

        CompletableFuture wf1 = CompletableFuture.runAsync(()-> { fileSystem.write("/word1",getFileContents("/home/manoj/data/words1.txt"), FileSystem.WriteMode.OverWrite); });
        CompletableFuture wf2 = CompletableFuture.runAsync(()->{fileSystem.write("/home/word2",getFileContents("/home/manoj/data/words2.txt"), FileSystem.WriteMode.OverWrite); });

        CompletableFuture wf3 = CompletableFuture.runAsync(()-> { fileSystem.write("/home/data/word3",getFileContents("/home/manoj/data/words3.txt"), FileSystem.WriteMode.Append); });

        CompletableFuture.allOf(wf1,wf2,wf3).join();

        fileSystem.write("/home/data/word3",getFileContents("/home/manoj/data/words3.txt"), FileSystem.WriteMode.OverWrite);

      fileSystem.printFiles();


        //fileSystem.read("/home/word2").forEach(System.out::println);

        fileSystem.deleteFile("/home/data/word3");



        fileSystem.deleteDirAndUnderlying("/home/");

       System.out.println("======================================================");

       fileSystem.printDirs();



// "org/apache/tika/mime/custom-mimetypes.xml"*/


    }
}
