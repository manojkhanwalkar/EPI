package DP;

public class MaxSum {


    public static void main(String[] args) {

        int[] arr = {904,40,523,12,-335,-385,-124,481,-31};

        int max = arr[0];
        int curr = arr[0];

        for (int i=1;i<arr.length;i++)
        {
             curr = Math.max(curr+arr[i],arr[i]);
            if (curr>max) max = curr;
        }

        System.out.println(max);




    }

    /*

    public static void main(String[] args) {

        int[] arr = {904,40,523,12,-335,-385,-124,481,-31};


        int running =0, min =0, max =0;
        for (int i : arr)
        {
            running+=i;
            if (running<min)
                min = running;

            if (running-min>max)
            {
                max = running-min;
            }

        }


        System.out.println(max);


    }
     */



}
