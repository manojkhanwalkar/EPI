package java891011121314.phaserframework;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CHM<K,V>  {

    int buckets = 16;
    int size = 1024;

    public CHM()
    {
        init();
    }


    public CHM(int buckets , int size)
    {
        this.buckets=buckets;
        this.size=size;
        init();

    }

    Lock[] locks ;
    Map<K,V>[] maps;

    private void init()
    {
        locks = new Lock[buckets];
        maps = (Map<K, V>[]) new  Map<?,?>[buckets];

        for (int i=0;i<buckets;i++)
        {
            locks[i] = new ReentrantLock();
            maps[i] = new HashMap<>();
        }
    }

    private int getBucket(K key)
    {
        int bucket = key.hashCode()%buckets;

        return Math.abs(bucket);
    }


    public V get(K key)
    {
        System.out.println("Getting value for " + key);

        int bucket = getBucket(key);

        Lock lock = locks[bucket];
        try {

            lock.lock();
        return maps[bucket].get(key);
    }finally {
        lock.unlock();
    }
    }


    public void put(K key , V value)
    {

        System.out.println("Putting value " + value);
        int bucket =getBucket(key);
        Lock lock = locks[bucket];
        try {

            lock.lock();
            maps[bucket].put(key, value);

        }finally {
            lock.unlock();
        }
    }


    public V remove(K key)
    {
        int bucket = getBucket(key);
        Lock lock = locks[bucket];
        try {

            lock.lock();
        return maps[bucket].remove(key);

        }finally {
            lock.unlock();
        }

    }


    public static void main(String[] args) throws Exception {

        final CHM<Long,Long> map = new CHM<>();


   /*     for (int i=0;i<100;i++)
        {
            long time = System.nanoTime();

            map.put(time,time);

            long time1 = System.currentTimeMillis();

            map.put(time1,time);

            System.out.println(time + " "  + map.get(time) );

            System.out.println(time1 + "  " +  map.get(time1) );

            System.out.println("Finished task ");
        }*/

        ExecutorService service = Executors.newCachedThreadPool();

        AtomicInteger count = new AtomicInteger();

        for (int i=0;i<100;i++)
        {
            service.submit(()->{



                long time = System.nanoTime();

                map.put(time,time);

                long time1 = System.currentTimeMillis();

                map.put(time1,time);

                System.out.println(time + " "  + map.get(time) + " " + count.getAndIncrement());

                System.out.println(time1 + "  " +  map.get(time1) + " " + count.getAndIncrement());

                System.out.println("Finished task ");


            });
        }


        Thread.sleep(10000);



    }



}
