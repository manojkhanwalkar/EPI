package bittorrent;

import java.util.ArrayList;
import java.util.List;

public class FileContents {

Tracker tracker ;

List<String> contents;  // index is the implied chunk number .


    public FileContents(Tracker tracker, List<String> contents) {
        this.tracker = tracker;
        this.contents = contents;
    }


  /*  public FileContents(Tracker tracker, List<List<String>> parcontents) {
        this.tracker = tracker;
        this.contents = new ArrayList<>();

        parcontents.stream().forEach(list-> {

          contents.addAll(list);

        });
    }*/

  public FileContents(Tracker tracker)
  {
      this.tracker = tracker;
      this.contents = new ArrayList<>();
  }

  public void addPartial(List<String> parContents)
  {
      contents.addAll(parContents);
  }


    public FileContents() {
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "FileContents{" +
                "tracker=" + tracker +
                '}';
    }

    public String getContents(int num)
    {
        return contents.get(num);
    }

}
