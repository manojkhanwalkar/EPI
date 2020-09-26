package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BSearch{


    public int bsearch(int value , List<Integer> values)
    {
        int low = 0; int high = values.size()-1;

        while(low<=high)
        {
            int mid = low + (high-low)/2;
            if (values.get(mid)==value) {
                return mid;
            }
            if (values.get(mid) < value)
            {
                low = mid+1;
            }
            else
            {
                high=mid-1;
            }
        }

        return -1;
    }

    public int firstOccurenceOf(int value, List<Integer> values)
    {
        int pos = bsearch(value,values);
        if (pos>0)
        {
            while(values.get(pos-1)==value)
            {
                pos--;
            }
        }

        return pos;
    }

    public int searchEntrySameAsIndex(List<Integer> values)
    {

        int low=0; int high = values.size()-1;
        while(low<=high)
        {
            int mid = low + (high-low)/2;
            if (values.get(mid)==mid)
                return mid;

            if (values.get(mid)> mid)
            {
                high = mid-1;
            }
            else
            {
                low = mid+1;
            }

        }

        return -1;
    }

    public int cyclicArrayStartPos(List<Integer> values)
    {
        int low=0; int high=values.size()-1;
        while(low<high)
        {
            int mid = low + (high-low)/2;
            if (values.get(mid)>values.get(high))
            {
                low = mid+1;
            }
            else
            {
                high = mid;
            }
        /*    else if (values.get(low) < values.get(mid) && values.get(mid) < values.get(high))
            {
                return low;
            }
            else
            {
                System.out.println(low + "  " + mid + "  " + high);
            }*/
        }

        return low;
    }

    public int squareLessThan(int k)
    {
        int low = 0; int high = k;
        while(low<=high)
        {
            int mid = low+(high-low)/2;

            int square = mid*mid;
            if (square==k)
                return mid;
            if (square<k)
            {
                low = mid+1;
            }
            else
            {
                high = mid-1;
            }
        }

        return low-1;
    }

    public static void main(String[] args) {


        BSearch bSearch = new BSearch();

     //   List<Integer> values = List.of(-14,-10,2,108,108,243,285,285,285,401);


         // List<Integer> values = List.of(378,478,550,631,103,203,220,234,279,368);


         // System.out.println(bSearch.cyclicArrayStartPos(values));

        System.out.println(bSearch.squareLessThan(100));
        System.out.println(bSearch.squareLessThan(101));
        System.out.println(bSearch.squareLessThan(99));

   /*     Random random = new Random();

        for (int i=0;i<10;i++)
        {
            //int num = random.nextInt(10);
            values.add(i);
        }

        Collections.Utility(values);

        System.out.println(bSearch.bsearch(1,values));
        System.out.println(bSearch.bsearch(10,values));
        System.out.println(bSearch.bsearch(5,values));
        System.out.println(bSearch.bsearch(7,values));


        System.out.println(bSearch.firstOccurenceOf(108,values));
        System.out.println(bSearch.firstOccurenceOf(285,values));
        System.out.println(bSearch.firstOccurenceOf(-1,values));
*/

      //  System.out.println(bSearch.searchEntrySameAsIndex(values));

    }
}
