package invariants;

import java.util.Arrays;

public class TwoSum {

    public static void main(String[] args) {

        int[] arr = {-2,1,2,4,7,11};

        System.out.println(threeSum(arr,33));


    }

    public static boolean threeSum(int[] arr, int sum)
    {
            for (int i=0;i<arr.length;i++)
            {
                if (twoSum(arr,sum-arr[i]))
                        return true;
            }

            return false;
    }


    public static boolean twoSum(int[] arr, int sum)
    {
        Arrays.sort(arr);

        int i=0, j=arr.length-1;

        if (sum <= arr[i] )
            return false;

        while(i<=j)
        {
            int curr = arr[i] + arr[j];
            if (curr==sum)
                return true;
            if (curr > sum )
                j--;
            else
                i++;
        }

        return false;

    }

}
