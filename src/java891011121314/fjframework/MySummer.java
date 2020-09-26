package java891011121314.fjframework;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MySummer extends RT<Long> {

    int start;
    int end;
    long[] numbers ;
    public MySummer(int max)
    {
        numbers = new long[max];

        for (int i=0;i<max;i++)
        {
            numbers[i] = i;
        }

        start=0;
        end=max-1;
    }

    @Override
    public String toString() {
        return "MySummer{" +
                "start=" + start +
                ", end=" + end +
                " " + Thread.currentThread().getName() +
                '}';
    }

    private MySummer(int start, int end, long[] numbers)
    {
        this.start=start;
        this.end=end;
        this.numbers = numbers;
    }

    public static void main(String[] args) {

        // create first task and put it in FJ pool

       System.out.println( new FJ().invoke(new MySummer(10001)));
    }


    public static final int THRESHOLD = 1000;

    @Override
    protected Long compute() {

        int len = end-start;

        long sum=0;
        if (len<THRESHOLD)
        {
            for (int i=start;i<=end;i++)
            {
                sum+=numbers[i];
            }

            return sum;

        }

        int mid = start+(end-start)/2;

        MySummer left = new MySummer(start,mid, numbers);
        MySummer right = new MySummer(mid+1,end,numbers);

        left.fork();

        right.fork();


        long rightResult = right.join();

        long leftResult = left.join();

        return leftResult+rightResult;


    }
}
