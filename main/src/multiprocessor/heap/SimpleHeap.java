package multiprocessor.heap;

import java.util.Arrays;

public class SimpleHeap<T> {

    public static void main(String[] args) {


        SimpleHeap<String> simpleHeap = new SimpleHeap<>(128);

        for (int i=0;i<100;i++)
        {
            simpleHeap.add(null,100-i);
        }

       simpleHeap.print();

        for (int i=0;i<100;i++)
        {
            int num = simpleHeap.removeMin();
            System.out.println(num);
        }


    }


    int maxCapacity;

    static final int ROOT = 1;

    int bottom=0;

    HeapNode<T>[] nodes ;

    public SimpleHeap(int maxCapacity)
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

        bottom++;
        HeapNode<T> node = new HeapNode<>(item,score);
        nodes[bottom] = node;
        if (bottom==ROOT)
        {
            return;
        }

        // swim upwards till invariance is met .

        int child = bottom;
        int parent = child/2;
        while(parent>=ROOT)
        {
            if (nodes[parent].score> nodes[child].score)
            {
                HeapNode tmp = nodes[parent];
                nodes[parent] = nodes[child];
                nodes[child] = tmp;
                child = parent;
                parent = child/2;

            }
            else
            {
                break;
            }

        }

    }


    public Integer removeMin()
    {
        if (bottom<ROOT)
            return null;

        bottom--;

        int score = nodes[ROOT].score;
        if (bottom<ROOT)  // last element
        {
            return score;
        }

        nodes[ROOT] = nodes[bottom+1];

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


            if (nodes[parent].score > nodes[child].score) {
                // swap parent and left child
                HeapNode tmp = nodes[parent];
                nodes[parent] = nodes[child];
                nodes[child] = tmp;
                parent = child;


            } else {
                break;
            }

        }

        return score;

    }

}
