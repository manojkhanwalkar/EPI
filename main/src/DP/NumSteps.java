package DP;

public class NumSteps {


    public static void main(String[] args) {

        System.out.println(numSteps(3,3));

    }

    public static int numSteps(int steps , int totSteps)
    {

        int[] arr = new int[totSteps+1];

        // base condition
        arr[0] = 0;
        for (int i=1;i<=steps;i++)
        {
            arr[i] = 1 ;
            for (int j=i;j>0;j--)
            {
                arr[i] = arr[i] + arr[j-1];
            }
        }

        for (int i=steps+1;i<=totSteps;i++)
        {
            arr[i] = 0;
            for (int j=i;j>i-steps;j--)
            {
                arr[i]= arr[i]+arr[j-1];
            }
        }

        return arr[totSteps];

    }
}
