package rfs;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RFSFileWriter implements  Closeable {

    //todo - write on flush , write will just collect till flush or close is called , close check if already flushed , if so do nothing .
    //TODO - on flush clear the contents list so that its not written again .

    FileSystem fileSystem;
    String fileName;

    List<String> contents;

    protected RFSFileWriter(String fileName, FileSystem fileSystem )
    {
        this.fileSystem = fileSystem;
        this.fileName = fileName;

        contents = new ArrayList<>();

        fileSystem.createFile(fileName);


    }

    @Override
    public void close() throws IOException {
        flush();
        contents = null;
    }



   public void write(String str)
   {
       contents.add(str);
   }

   private void flush()
   {
       fileSystem.write(fileName,contents, FileSystem.WriteMode.OverWrite);
       contents.clear();
   }


}
