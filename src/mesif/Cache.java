package mesif;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static mesif.Memory.BUFSIZE;

public class Cache {

    static int CACHELINES = 4;

    Memory memory = Memory.getInstance();


    CacheLine[] cacheLines = new CacheLine[CACHELINES];

    Set<Integer> cacheLineContents = new HashSet();

    //boolean[] cacheLineStatus = new boolean[CACHELINES];

    Directory directory;

    public Cache()
    {
        for (int i=0;i<CACHELINES;i++)
        {
            cacheLines[i] = new CacheLine();

        }

        directory = Directory.getInstance();
        directory.register(this);
    }


    public boolean put(int start, char[] data) {
        // memory.put(start,data);
        int index = start % (BUFSIZE-1) % CACHELINES;


        if (cacheLines[index].status!=CacheLineStates.Exclusive)
        {
  //          while(directory.makeExclusive(start,this)) ;

    //        refreshFromMem = true;

            return false;
        }


//        if (cacheLines[index].status==CacheLineStates.Exclusive)
        {
            if(!directory.getModified(start,this))
                return false;

            {


                cacheLines[index].put(data);
                cacheLines[index].address = start;
                cacheLineContents.add(start);
                cacheLines[index].status = CacheLineStates.Modified;


                flush(start,data);

                directory.invalidate(start,this);
                cacheLines[index].status = CacheLineStates.Invalid;

                return true;
            }
        }

     //   System.out.println("Unexpected error " + start + "  " + index + "  " + cacheLines[index].status);


    }

    public void invalidate(int start)
    {
        if (cacheLineContents.contains(start)) {
            int index = start % (BUFSIZE-1) % CACHELINES;
            cacheLines[index] = new CacheLine();
        }

    }

    public void shared(int start)
    {

        if (cacheLineContents.contains(start)) {
            int index = start %( BUFSIZE-1) % CACHELINES;
            cacheLines[index].status = CacheLineStates.Shared;
        }



    }



    private void flush(int start, char[] data)
    {
        memory.put(start,data);
    }

    public char[] get(int start)
    {

        // check if exists in cache line - yes , get from there. No get from memory.
        int index = start%(BUFSIZE-1)%CACHELINES;

        if (cacheLineContents.contains(start))
        {
            System.out.println("Returned from cache line " + start);
            return cacheLines[index].get();
        }
        else
        {
            CacheLineStates status = directory.getExclusiveOrShared(start,this);
            char[] buf = memory.get(start);

            cacheLineContents.remove(cacheLines[index].address);

            if (cacheLines[index].status==CacheLineStates.Modified)
            {
                flush(cacheLines[index].address,cacheLines[index].get());
                directory.invalidate(start,this);
            }

            if (cacheLines[index].status==CacheLineStates.Exclusive)
            {
                directory.invalidate(start,this);
            }

            if (cacheLines[index].status==CacheLineStates.Shared)
            {
                // TODO - need to check what to do about shared
            }

            cacheLines[index].put(buf);
            cacheLines[index].address = start;
            cacheLines[index].status = status;

            return buf;

        }


    }


    static class CacheUser implements Runnable
    {
        int start;  char startChar;  Cache cache; int count = 26;

        public CacheUser(int start, char startChar, Cache cache)
        {
            this.start = start;
            this.startChar = startChar;
            this.cache = cache;
        }

        public CacheUser(int start, char startChar, Cache cache, int count)
        {
            this.start = start;
            this.startChar = startChar;
            this.cache = cache;
            this.count = count;
        }


        public void run()
        {

            //System.out.println(startChar);
            for (int i=0;i<100;i++)
            {
                while(true) {
                    char[] data = cache.get(BUFSIZE * i);

                    for (int j = 0; j < count; j++) {
                        data[j + start] = (char) ((int) startChar + j);
                    }

                    if (cache.put(BUFSIZE * i, data))
                        break;
                }


            }

        }
    }

    public static void main(String[] args) {

        Cache cache1 = new Cache();

        Cache cache2 = new Cache();

        Cache cache3 = new Cache();

     // CompletableFuture.runAsync(new CacheUser(0,'a',cache1)).thenRunAsync(new CacheUser(26,'A',cache2)).join();

       CompletableFuture<Void> f1 = CompletableFuture.runAsync(new CacheUser(0,'a',cache1));
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(new CacheUser(26,'A',cache2));
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(new CacheUser(52,'0',cache3,10));


        CompletableFuture.allOf(f1,f2,f3).join();

        cache1.memory.dump();



    }
}
