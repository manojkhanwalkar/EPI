package DP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinWeightPath {


    public static void main(String[] args) {

        List<List<Integer>>  triangle = new ArrayList<>();
        for (int i=0;i<5;i++)
        {
            triangle.add(new ArrayList<>());
        }

        triangle.get(0).add(2);

        triangle.get(1).add(4);
        triangle.get(1).add(4);

        triangle.get(2).add(8);
        triangle.get(2).add(5);
        triangle.get(2).add(6);

        triangle.get(3).add(4);
        triangle.get(3).add(2);
        triangle.get(3).add(6);
        triangle.get(3).add(2);

        triangle.get(4).add(1);
        triangle.get(4).add(5);
        triangle.get(4).add(2);
        triangle.get(4).add(3);
        triangle.get(4).add(4);


        List<Integer> path = new ArrayList<>();

        List<Integer> prev;

        path.add(triangle.get(0).get(0));

        prev = path;

        for (int i=1;i<triangle.size();i++)
        {
            path = new ArrayList<>();

            List<Integer> curr = triangle.get(i);
            for (int j=0;j<curr.size();j++)
            {
                int sum1=Integer.MAX_VALUE;
                int sum2=Integer.MAX_VALUE;

                if (j<prev.size())
                {
                    sum1 = curr.get(j) + prev.get(j);
                }
                if (j!=0)
                {
                    sum2 = curr.get(j) + prev.get(j-1);
                }

                path.add(Math.min(sum1,sum2));

            }

            prev = path;
        }

        System.out.println(Collections.min(prev));

    }
}
