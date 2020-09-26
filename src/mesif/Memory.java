package mesif;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Memory {

    static class MemoryHolder
    {
        static Memory Instance = new Memory();
    }

    public static Memory getInstance()
    {
        return MemoryHolder.Instance;
    }

    static final int BUFSIZE = 64;
    static final int MEMSIZE = BUFSIZE*100;

    char[] buf = new char[MEMSIZE];

    ReadWriteLock[] locks = new ReadWriteLock[MEMSIZE/BUFSIZE];

    private Memory()
    {
        for (int i=0;i<locks.length;i++)
        {
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    public void put(int start, char[] data)
    {


        int index = start/BUFSIZE;

        try {

            locks[index].writeLock().lock();
            int j = start;
            for (int i = 0; i < BUFSIZE; i++) {
                buf[j + i] = data[i];
            }

        } finally {
            locks[index].writeLock().unlock();
        }

    }

    public char[] get(int start)
    {
        int index = start/BUFSIZE;
        try{
            locks[index].readLock().lock();
            char[] data = new char[BUFSIZE];
            int j=start;

            for (int i=0;i<BUFSIZE;i++)
            {
                data[i] = buf[j+i];
            }

            return data;
        } finally {
            locks[index].readLock().unlock();
        }

    }


    private void LockAll()
    {

            for (int i=0;i<locks.length;i++)
            {
                locks[i].writeLock().lock();
            }
    }


    private void UnLockAll()
    {
        for (int i=0;i<locks.length;i++)
        {
            locks[i].writeLock().unlock();
        }

    }

    public void dump()
    {
        LockAll();

        StringBuilder builder = new StringBuilder();
        builder.append(buf);

        System.out.println(builder.toString());

        UnLockAll();
    }


    public static void main(String[] args) {

        Memory memory = new Memory();

        for (int i=0;i<100;i++)
        {
            char[] data = memory.get(BUFSIZE*i);
            for (int j=0;j<26;j++)
            {
                data[j] = (char)((int)'a' + j);
            }

            memory.put(BUFSIZE*i, data);
        }


        memory.dump();



    }

}
