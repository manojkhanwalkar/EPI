package bittorrent;

import java.util.Arrays;
import java.util.Objects;

public class Tracker {

    String fileName;

    String[] fileTags;

    int totalChunks;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getFileTags() {
        return fileTags;
    }

    public void setFileTags(String[] fileTags) {
        this.fileTags = fileTags;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }


    @Override
    public String toString() {
        return "Tracker{" +
                "fileName='" + fileName + '\'' +
                ", fileTags=" + Arrays.toString(fileTags) +
                ", totalChunks=" + totalChunks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracker tracker = (Tracker) o;
        return fileName.equals(tracker.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
