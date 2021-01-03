package java891011121314;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MySummer extends RecursiveTask<Long> {

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

    private MySummer(int start,int end, long[] numbers)
    {
        this.start=start;
        this.end=end;
        this.numbers = numbers;
    }

    public static void main(String[] args) {

        // create first task and put it in FJ pool

       System.out.println( new ForkJoinPool().invoke(new MySummer(10001)));
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

        long leftResult = left.fork().join();

        long rightResult = right.compute();

        return leftResult+rightResult;


    }
}
