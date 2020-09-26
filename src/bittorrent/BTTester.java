package bittorrent;

public class BTTester {

    public static void main(String[] args) throws Exception {

        BTPeer peer1 = new BTPeer("Peer1");
        peer1.add("/home/manoj/data/torrent/tor1", "tag1,tag11");

        BTPeer peer2 = new BTPeer("Peer2");
        peer2.add("/home/manoj/data/torrent/tor2", "tag2,tag22");
        peer2.add("/home/manoj/data/torrent/tor3", "tag3,tag33");

        BTPeer peer3 = new BTPeer("Peer3");
        peer3.add("/home/manoj/data/torrent/tor3", "tag3,tag33");

        peer3.add("/home/manoj/data/torrent/tor4", "tag4,tag44");

       // System.out.println(peer1.getTrackersAndPeers());


        System.out.println(peer1.filesForServing.size());

        peer1.getFilesFromPeers();
        System.out.println(peer1.filesForServing.size());


        BTPeer peer = new BTPeer("Peer");

        System.out.println(peer.filesForServing.size());

        peer.getFilesFromPeers();
        System.out.println(peer.filesForServing.size());



    }
}
