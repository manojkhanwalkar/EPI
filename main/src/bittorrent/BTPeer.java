package bittorrent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BTPeer {


    volatile List<FileContents> filesForServing = new ArrayList<>();

    String name ;



    public BTPeer(String name)
    {
        this.name = name;
        BTRegistry.getInstance().register(this);

    }

    public FileChunk getChunk(String fileName, int chunk)
    {
        var res = filesForServing.stream().filter(fileContents-> fileContents.getTracker().getFileName().equals(fileName)).map(fc->fc.getContents(chunk)).findFirst();

        return new FileChunk(fileName,chunk,res.get());
    }

    public synchronized List<Tracker> getTrackers()
    {
       return  filesForServing.stream().map(fileContents -> fileContents.getTracker()).collect(Collectors.toList());
    }

    public void add(Tracker tracker, FileContents fileContents )
    {
        filesForServing.add(fileContents);
    }

    public void add(String fileName, String... fileTags) throws Exception
    {

        Tracker tracker = new Tracker();
        tracker.setFileName(fileName);
        tracker.setFileTags(fileTags);

        FileContents fileContents = new FileContents();

        List<String> contents = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
                contents.add(line);
                line = reader.readLine();
            }


        }

        tracker.setTotalChunks(contents.size());

        fileContents.setContents(contents);
        fileContents.setTracker(tracker);

        filesForServing.add(fileContents);


    }

    public Map<Tracker,List<BTPeer>>  getTrackersAndPeers()
    {
        Map<Tracker,List<BTPeer>>  trackerPeers = new HashMap<>();
        // find other peers , see what trackers they have and download files from them
        BTRegistry.getInstance().getPeers(this).forEach(peer->{

            peer.getTrackers().stream().forEach(tracker-> {
               var l=  trackerPeers.computeIfAbsent(tracker,(t)-> new ArrayList<>());
               l.add(peer);

            });
        });

        return  trackerPeers;
    }

    public void getFilesFromPeers()
    {
        Map<Tracker,List<BTPeer>>  trackersAndPeers = getTrackersAndPeers();
        // for each tracker , get file from first peer .



        trackersAndPeers.entrySet().stream().forEach(entry->{

            Tracker t = entry.getKey();

            List<BTPeer> peers = entry.getValue();

            int peerChunk = t.getTotalChunks()/peers.size();

            List<CompletableFuture<List<String>>> futures = new ArrayList<>();

            for (int i=0;i<peers.size();i++)
            {
                BTPeer peer = peers.get(0);
                int start = peerChunk*i;   int end = peerChunk*(i+1);

                var future = CompletableFuture.supplyAsync(()->{
                    List<String> contents = new ArrayList<>();

                    for (int j=start;j<end;j++)
                    {
                        String s  = peer.getChunk(t.getFileName(),j).getContent();
                        contents.add(s);
                    }

                    return contents;

                });

                futures.add(future);


            }

            FileContents fileContents = new FileContents(t);




            futures.stream().forEach(f->{

                try {
                    fileContents.addPartial(f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });






            add(t,fileContents);

        });
    }


    @Override
    public String toString() {
        return "BTPeer{" +
                "name='" + name +
                '}';
    }
}
