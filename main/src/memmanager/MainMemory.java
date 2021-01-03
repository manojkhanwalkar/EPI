package memmanager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// ASSUMPTION is that swap will not run out of free pages - so evict will not fail

public class MainMemory {

     final int MAXMEMCAPACITY=100;

    MemPage[] memPages = new MemPage[MAXMEMCAPACITY];
    Lock[] memPageLocks = new ReentrantLock[MAXMEMCAPACITY];

    List<MemPage> freePages = new LinkedList<>();

    Lock freePagesLock = new ReentrantLock();

    static class MainMemoryHolder
    {

        static MainMemory INSTANCE = new MainMemory();
    }

    public static MainMemory getInstance()
    {
        return MainMemoryHolder.INSTANCE;
    }

    private MainMemory()
    {
        for (int i=0;i<MAXMEMCAPACITY;i++)
        {
            memPages[i] = new MemPage(i);
            memPageLocks[i] = new ReentrantLock();
        }
        for (MemPage page : memPages)
        {
            freePages.add(page);
        }

    }


    TLB tlb = new TLB();

    public void register(String procName)
    {
        tlb.register(procName);
    }

    public void write(String procName , int page , int offset, char c )
    {
        freePagesLock.lock();

        MemPage memPage = getMemPage(procName,page);

        memPage.write(offset,c);

        freePagesLock.unlock();


    }


    public char read(String procName , int page , int offset)
    {

        try {
            freePagesLock.lock();
            MemPage memPage = getMemPage(procName, page);

            return memPage.read(offset);

        } finally {
            freePagesLock.unlock();
        }


    }


    private MemPage getMemPage(String procName , int page)
    {
        tlb.lock.lock();
        MemPage memPage;
        TLBEntry entry = tlb.get(procName,page);
        if (entry==null)
            entry = new TLBEntry();



        if (entry.memPageNumber==-1 && entry.swapPageNumber ==-1)  // no memory page allocated
        {
            memPage = allocate();
            entry.memPageNumber = memPage.pageNum;
            tlb.put(procName,page,entry);
        }
        else if (entry.memPageNumber!=-1)
        {
            memPage = memPages[entry.memPageNumber];
        } else {
            // page in swap space .
            memPage = allocateFromSwap(entry);
        }

        tlb.lock.unlock();

        return memPage;

    }

    private MemPage allocateFromSwap(TLBEntry tlbEntry)
    {
        if (freePages.isEmpty())  // evict first
        {
            TLBEntry entry = tlb.getEntryToEvict();
            MemPage memPage = memPages[entry.memPageNumber];
            evict(memPage,entry);
            memPage = freePages.remove(0);
            return memPage;
        }
        else
        {

            MemPage memPage = freePages.remove(0);

            recover(memPage,tlbEntry);
            return memPage;

        }
    }


    private MemPage allocate()
    {
        // if pages are free , allocate , else evict a page into swap memory and then allocate that page . eviction is LRU
        if (!freePages.isEmpty())
        {
            MemPage memPage = freePages.remove(0);
            return memPage;
        }
        else
        {
            TLBEntry entry = tlb.getEntryToEvict();
            MemPage memPage = memPages[entry.memPageNumber];
            evict(memPage,entry);

            memPage = freePages.remove(0);
            return memPage;
        }
    }





    public void evict(MemPage memPage, TLBEntry entry)
    {


        int index = SwapMemory.getInstance().evict(memPage);

        entry.swapPageNumber=index;   // to recover

        freePages.add(memPage);

    }




    public void recover(MemPage memPage, TLBEntry entry)
    {
        SwapMemory.getInstance().recover(entry.swapPageNumber,memPage);
        entry.memPageNumber = memPage.pageNum;
    }
}
