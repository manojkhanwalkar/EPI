package multiprocessor.heap;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentHeap<T> {

    public static void main(String[] args) {


        ConcurrentHeap<String> concurrentHeap = new ConcurrentHeap<>(128);

        for (int i=0;i<100;i++)
        {
            concurrentHeap.add(null,100-i);
        }

        concurrentHeap.print();

        for (int i=0;i<100;i++)
        {
            int num = concurrentHeap.removeMin();
            System.out.println(num);
        }


    }


    Lock lock = new ReentrantLock();


    int maxCapacity;

    static final int ROOT = 1;

    int bottom=0;

    HeapNode<T>[] nodes ;

    public ConcurrentHeap(int maxCapacity)
    {
        this.maxCapacity = maxCapacity+1;
        nodes =  (HeapNode<T>[])new HeapNode[this.maxCapacity];
    }



    public void print()
    {
        Arrays.stream(nodes).limit(bottom).forEach(System.out::println);
    }


    public void add(T item , int score)
    {

        lock.lock();
        bottom++;
        HeapNode<T> node = new HeapNode<>(item,score);
        nodes[bottom] = node;
        if (bottom==ROOT)
        {
            lock.unlock();
            return;
        }

        lock.unlock();

        // swim upwards till invariance is met .



        int child = bottom;
        int parent = child/2;
        while(parent>=ROOT)
        {
            Lock parentLock=null;
            Lock childLock=null;

            try {
                 parentLock = nodes[parent].lock;
                 childLock = nodes[child].lock;
                parentLock.lock(); childLock.lock();

                if (nodes[parent].score > nodes[child].score) {
                    HeapNode tmp = nodes[parent];
                    nodes[parent] = nodes[child];
                    nodes[child] = tmp;
                    child = parent;
                    parent = child / 2;

                } else {
                    break;
                }

            }finally {

                parentLock.unlock(); childLock.unlock();
            }

        }

    }


    public Integer removeMin()
    {
        int score =0;
        try {

            lock.lock();
            if (bottom < ROOT)
                return null;

            bottom--;

             score = nodes[ROOT].score;
            if (bottom < ROOT)  // last element
            {
                return score;
            }

            nodes[ROOT] = nodes[bottom + 1];

        } finally {
            lock.unlock();
        }
        // take last element and percolate downwards to correct position.

        int parent = ROOT;
        while(true) {
            int left = parent * 2;
            int right = left + 1;

            int child ;

            if (right>bottom)
                break;

            if (nodes[left]==null)
                break;

            if (nodes[right]==null)
            {
                child = left;
            }
            else
            {
                child = (nodes[left].score<nodes[right].score? left:right);
            }

            Lock parentLock = null;  Lock childLock = null;
            try {
                 parentLock = nodes[parent].lock;
                 childLock = nodes[child].lock;

                parentLock.lock();
                childLock.lock();

                if (nodes[parent].score > nodes[child].score) {
                    // swap parent and left child
                    HeapNode tmp = nodes[parent];
                    nodes[parent] = nodes[child];
                    nodes[child] = tmp;
                    parent = child;


                } else {
                    break;
                }

            } finally {
                parentLock.unlock();
                childLock.unlock();
            }

        }

        return score;

    }

}
