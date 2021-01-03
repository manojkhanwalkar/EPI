package DP;

import java.util.*;

public class LongestRunningSequence {


        public static void main(String[] args) {

        int[] arr = { 1,5,8,7,10,9,12};

        int [] count = new int[arr.length];

        for (int i=0;i<count.length;i++)
            count[i]=1;

        for (int i=1;i<count.length;i++)
        {
            for (int j=0;j<i;j++)
            {
                if (arr[j] < arr[i])
                {
                    count[i] = Math.max(count[i],count[j]+1);
                }
            }
        }

      //
            for (int i=0;i<arr.length;i++)
            {
                System.out.println(arr[i] + "==> " + count[i]);
            }


    }


  //  {1=1, 5=2, 7=3, 8=3, 9=4, 10=4, 12=5}

  /*  public static void main(String[] args) {

        int[] arr = { 1,5,8,7,10,9,12};

        Map<Integer,Integer> count = new HashMap<>();


        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i=0;i<arr.length;i++)
        {
            count.put(arr[i],1);
            adjacent.add(new ArrayList<>());
            for (int j=0;j<arr.length;j++)
            {
                if (arr[i] < arr[j] && i<j)
                {
                    adjacent.get(i).add(arr[j]);
                }
            }
        }

        // process the dag

        for (int i=0;i<adjacent.size();i++)
        {
            List<Integer> list1 = adjacent.get(i);
            int start = count.get(arr[i]);
            for (int j=0;j<list1.size();j++)
            {
                int curr = count.get(list1.get(j));
                if (curr<start+1)
                    count.put(list1.get(j),start+1);

            }
        }

        System.out.println(count);


    }*/
}
