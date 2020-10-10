package memmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class TLB {

    Lock lock = new ReentrantLock();

    Map<String, Map<Integer,TLBEntry>>  entries = new HashMap<>();

    public void register(String procName)
    {
        entries.put(procName, new HashMap<>());
    }

    ThreadLocalRandom random = ThreadLocalRandom.current();

    TLBEntry getEntryToEvict()
    {
       var list =  entries.values().stream().flatMap(e->e.values().stream()).collect(Collectors.toList());

       int next = random.nextInt(list.size());

       return list.get(next);
    }


    public TLBEntry get(String procName , int pageNum)
    {
        var entry = entries.get(procName);
        if (entry!=null)
        {
            var tlbEntry = entry.get(pageNum);
            return  tlbEntry;

        }
        else
        {
            throw new RuntimeException("Process has not been registered with the TLB");


        }

    }


    public void put(String procName, int pageNum , TLBEntry entry)
    {
        entries.get(procName).put(pageNum,entry);
    }



}
