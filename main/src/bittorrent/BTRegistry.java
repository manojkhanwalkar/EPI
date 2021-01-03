package bittorrent;

import com.amazonaws.services.simpleemail.model.IdentityNotificationAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BTRegistry {

    static class BTRegistryHolder {

        static BTRegistry INSTANCE = new BTRegistry();
    }


    public static BTRegistry getInstance()
    {
        return BTRegistryHolder.INSTANCE;
    }

    Set<BTPeer> btPeers = new HashSet<>();

    public synchronized void register(BTPeer peer)
    {
        btPeers.add(peer);

    }


    public synchronized List<BTPeer> getPeers(BTPeer peer)
    {
        // filter the input peer
       return btPeers.stream().filter(p->p!=peer).collect(Collectors.toList());
    }

}
