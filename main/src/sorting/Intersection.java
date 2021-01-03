package sorting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Intersection {


    public static void main(String[] args) {


        int[] arr1 = {2,3,3,5,5,6,7,7,8,12};
        int[] arr2 = {5,5,6,8,8,9,10,10};

        int i=0, j=0;

        Set<Integer> result = new HashSet<>();
        while(i<arr1.length && j<arr2.length)
        {
            if (arr1[i]==arr2[j])
            {
                result.add(arr1[i]);
                i++; j++;
            }

            if (arr1[i] > arr2[j])
                j++;
            else
                i++;
        }

        System.out.println(result);

    }
}
