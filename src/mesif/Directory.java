package mesif;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Directory {

    static class DirectoryHolder
    {
        static Directory directory = new Directory();
    }

    public static Directory getInstance()
    {
        return DirectoryHolder.directory;
    }

    private Directory()
    {

    }

    Set<Cache> caches = new HashSet<>();

    Map<Integer,CacheStatusTuple> addressOwner = new HashMap<>();

    public synchronized void register(Cache cache)
    {
        caches.add(cache);
    }


    public synchronized boolean  makeExclusive(int address , Cache cache)
    {
        CacheStatusTuple cacheStatusTuple = addressOwner.get(address);
        if (cacheStatusTuple==null)
        {
            addressOwner.put(address, new CacheStatusTuple(cache, CacheLineStates.Exclusive));
            return true;

        }
        else if (cacheStatusTuple.status==CacheLineStates.Modified || cacheStatusTuple.status==CacheLineStates.Exclusive) {

            return false; // someone else has it in exclusive or modified
        }

            for (Cache c : caches) {
                if (c != cache)
                    c.invalidate(address);
            }




        cacheStatusTuple.cache = cache;
        cacheStatusTuple.status = CacheLineStates.Exclusive;

        return true;


    }


    public synchronized CacheLineStates getExclusiveOrShared(int address , Cache cache)
    {

            // if not present , cache gets exclusive access , else shared access.

            CacheStatusTuple cacheStatusTuple = addressOwner.get(address);
            if (cacheStatusTuple != null) {
                switch (cacheStatusTuple.status) {
                    case Shared:
                        return CacheLineStates.Shared;
                    case Exclusive:
                        // notify cache holding exclusive that it is now shared
                        cacheStatusTuple.cache.shared(address);
                        cacheStatusTuple.status=CacheLineStates.Shared;
                        return CacheLineStates.Shared;
                    case Modified:
                        if (cacheStatusTuple.cache==cache)
                        {
                            cacheStatusTuple.status=CacheLineStates.Exclusive;
                            return CacheLineStates.Exclusive;
                        }
                        else {
                            return CacheLineStates.Invalid;   // cannot be shared at this time
                        }
                    case Invalid:
                        addressOwner.remove(address);
                        // fall thru for adding new entry
                }


            }


            addressOwner.put(address, new CacheStatusTuple(cache, CacheLineStates.Exclusive));
            return CacheLineStates.Exclusive;


    }

    // invalidate

    public synchronized void invalidate(int address, Cache cache)
    {
        // invalidate only if it exists and current cache is the owner in exclusive or modified mode
        CacheStatusTuple cacheStatusTuple = addressOwner.get(address);
        if (cacheStatusTuple!=null && cacheStatusTuple.cache==cache && (cacheStatusTuple.status==CacheLineStates.Exclusive|| cacheStatusTuple.status==CacheLineStates.Modified))
        {
            addressOwner.remove(address);
        }
    }

    public synchronized boolean getModified(int address , Cache cache)
    {


            CacheStatusTuple cacheStatusTuple = addressOwner.get(address);
            if (cacheStatusTuple != null) {
                switch (cacheStatusTuple.status) {
                    case Shared:
                        //TODO - invalidate all caches except requester
                        for (Cache c : caches) {
                            if (c != cache)
                                c.invalidate(address);
                        }

                        cacheStatusTuple.cache = cache;
                        cacheStatusTuple.status = CacheLineStates.Modified;

                        return true;
                    case Exclusive:
                        cacheStatusTuple.status = CacheLineStates.Modified;
                        return true;
                    case Modified:
                        if (cacheStatusTuple.cache != cache)
                            return false;
                        return true;
                    case Invalid:
                        addressOwner.remove(address);
                        // fall thru for adding new entry
                }

            }



            addressOwner.put(address, new CacheStatusTuple(cache, CacheLineStates.Modified));
            return true;


    }






    static class CacheStatusTuple
    {

        public CacheStatusTuple(Cache cache, CacheLineStates status) {
            this.cache = cache;
            this.status = status;
        }

        Cache cache;
        CacheLineStates status;
    }

}
