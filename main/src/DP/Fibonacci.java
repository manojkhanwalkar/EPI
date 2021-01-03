package DP;

public class Fibonacci {

    public static void main(String[] args) {

        calculateFibonacciUpto(10);

    }

    public static void calculateFibonacciUpto(int n)
    {

        int[] fib = new int[n+1];

        fib[0] = 0;
        fib[1] = 1 ;

        for (int i=2;i<=n;i++)
        {
            fib[i] = fib[i-1] + fib[i-2];
        }


        for (int i=0;i<=n;i++)
        {
            System.out.println(i + "--> " + fib[i]);
        }


    }


}
