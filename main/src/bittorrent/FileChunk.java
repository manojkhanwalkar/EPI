package bittorrent;

import java.util.stream.Stream;

public class FileChunk {
    int num;
    String content;
    String fileName;

    public FileChunk(String fileName, int chunk, String content) {
        this.fileName=fileName;
        num = chunk;
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
