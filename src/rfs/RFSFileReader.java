package rfs;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class RFSFileReader implements Iterable<String>, Closeable {

    FileSystem fileSystem;
    String fileName;

    List<String> contents;

    protected RFSFileReader(String fileName, FileSystem fileSystem)
    {
        this.fileSystem = fileSystem;
        this.fileName = fileName;

        contents = fileSystem.read(fileName);
    }

    @Override
    public void close() throws IOException {
        contents = null;
    }

    @Override
    public Iterator<String> iterator() {
        return new FileIterator(this);
    }


    static class FileIterator implements  Iterator<String>
    {
        int index=0;

        RFSFileReader fileReader;

        public FileIterator(RFSFileReader fileReader)
        {
            this.fileReader = fileReader;
        }

        @Override
        public boolean hasNext() {
            return (index<fileReader.contents.size());
        }

        @Override
        public String next() {
            return fileReader.contents.get(index++);
        }
    }



}
