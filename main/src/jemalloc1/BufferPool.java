package jemalloc1;


import java.util.ArrayList;
import java.util.List;

public class BufferPool {

    static class Holder
    {

        static BufferPool INSTANCE = new BufferPool(100);
    }

    final int[] sizes = {100,1000,10000,100000};

    List<List<LockedBuffer>> pools ;

    private BufferPool(int maxBuffers)
    {

        int size = 8* Runtime.getRuntime().availableProcessors();
        pools = new ArrayList<>(size);

        for (int i=0;i<size;i++)
        {
            pools.add(new ArrayList<>(sizes.length));
        }


        for (int i=0;i<pools.size();i++)
        {
            var pool = pools.get(i);
            for (int j=0;j<sizes.length;j++)
            {
                int bufSize = sizes[j];
                LockedBuffer lockedBuffer = new LockedBuffer(bufSize,maxBuffers);

                pool.add(lockedBuffer);

            }
        }



    }

    public static BufferPool getInstance()
    {
        return Holder.INSTANCE;
    }

    public Buffer get(int size)
    {
        int indexOfPool = (int)Thread.currentThread().getId()%pools.size();
        int indexOfSize =-1;
        for (int i=0;i<sizes.length;i++)
        {
            if (size<sizes[i])
            {
                indexOfSize = i;
                break;
            }

        }

        return pools.get(indexOfPool).get(indexOfSize).pop();


    }

    public void put(Buffer buffer)
    {
        int indexOfPool = (int)buffer.threadIdOfAllocation%pools.size();
        int indexOfSize =-1;
        for (int i=0;i<sizes.length;i++)
        {
            if (buffer.size()<sizes[i])
            {
                indexOfSize = i;
                break;
            }

        }

         pools.get(indexOfPool).get(indexOfSize).put(buffer);


    }

    public static void main(String[] args) {

        BufferPool bufferPool = BufferPool.getInstance();

        var b = bufferPool.get(120);

        bufferPool.put(b);

    }





}
