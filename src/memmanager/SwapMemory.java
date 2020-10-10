package memmanager;

import ucar.unidata.io.Swap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// ASSUMPTION is that swap will not run out of free pages - so evict will not fail

public class SwapMemory {

     final int MAXCAPACITY=1000;

    SwapPage[] swapPages = new SwapPage[MAXCAPACITY];
    Lock[] swapPageLocks = new ReentrantLock[MAXCAPACITY];

    List<SwapPage> freePages = new LinkedList<>();

    Lock freePagesLock = new ReentrantLock();

    static class SwapMemoryHolder
    {

        static SwapMemory INSTANCE = new SwapMemory();
    }

    public static SwapMemory getInstance()
    {
        return SwapMemoryHolder.INSTANCE;
    }

    private SwapMemory()
    {
        for (int i=0;i<MAXCAPACITY;i++)
        {
            swapPages[i] = new SwapPage(i);
            swapPageLocks[i] = new ReentrantLock();
        }
        for (SwapPage page : swapPages)
        {
            freePages.add(page);
        }

    }



    public int evict(MemPage memPage)
    {
        SwapPage swapPage;
        try {
            freePagesLock.lock();

             swapPage = freePages.remove(0);
            // get a free page
        } finally
        {
            freePagesLock.unlock();
        }

        try {
            swapPageLocks[swapPage.pageNum].lock();
            copyFromMem(swapPage, memPage);

        } finally {
            swapPageLocks[swapPage.pageNum].unlock();

        }

        return swapPage.pageNum;

    }

    private void copyFromMem(SwapPage swapPage , MemPage memPage)
    {

        System.arraycopy(memPage.buf,0,swapPage.buf,0,Page.size);
    }

    private void copyToMem(SwapPage swapPage , MemPage memPage)
    {
        System.arraycopy(swapPage.buf,0,memPage.buf,0,Page.size);
    }



    public void recover(int pageIndex, MemPage memPage)
    {
        SwapPage swapPage = swapPages[pageIndex];
        copyToMem(swapPage,memPage);
        freePages.add(swapPage);

    }
}
