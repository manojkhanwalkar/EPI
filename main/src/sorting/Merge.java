package sorting;

import java.util.Arrays;

public class Merge {


    public static void main(String[] args) {

        int [] arr1 =  { 5,13,17,0,0,0,0,0};
        int[] arr2 = { 3,7,11,19};

        // merge

        int j=arr2.length-1;
        int i = 2;

        int indx = 3+j;
        int tot = indx;

        for (int k=0;k<=tot;k++)
        {


            if (j<0 || (i>=0 && (arr1[i] > arr2[j] ))) {
                arr1[indx--] = arr1[i];
                i--;
            }
            else {
                arr1[indx--] = arr2[j];
                j--;
            }
        }

        Arrays.stream(arr1).forEach(n->System.out.println(n));


    }
}
