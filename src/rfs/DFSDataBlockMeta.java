package rfs;


// contains the file system where a range of blocks is stored .

// assume each file system stores contiguous blocks.


import com.fasterxml.jackson.annotation.JsonIgnore;

public class DFSDataBlockMeta {

/*  int blockStart;
  int blockEnd;
*/

@JsonIgnore
  transient FileSystem fileSystem ;

  String fileSystemName ;

    public DFSDataBlockMeta(int blockStart, int blockEnd, FileSystem fileSystem) {
   //     this.blockStart = blockStart;
     //   this.blockEnd = blockEnd;
        this.fileSystem = fileSystem;
        this.fileSystemName = fileSystem.fsName;
    }

/*
    public int getBlockStart() {
        return blockStart;
    }

    public void setBlockStart(int blockStart) {
        this.blockStart = blockStart;
    }

    public int getBlockEnd() {
        return blockEnd;
    }

    public void setBlockEnd(int blockEnd) {
        this.blockEnd = blockEnd;
    }
*/

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getFileSystemName() {
        return fileSystemName;
    }

    public void setFileSystemName(String fileSystemName) {
        this.fileSystemName = fileSystemName;
    }


    public DFSDataBlockMeta() {
    }
}
