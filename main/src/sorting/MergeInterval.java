package sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeInterval {

    static class Interval implements Comparable<Interval>{

        int left ;
        int right;

        public Interval(int left , int right)
        {
            this.left = left;

            this.right = right;
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }


        @Override
        public int compareTo(Interval interval) {
            return Integer.compare(left,interval.left);
        }

        public static Interval copy(Interval interval)
        {
            Interval newInterval = new Interval(interval.left,interval.right);
            return newInterval;
        }
    }


    public static List<Interval> test1()
    {
        List<Interval> intervals = new ArrayList<>();

        List<Interval> merged = new ArrayList<>();

        intervals.add(new Interval(2,4));

        intervals.add(new Interval(0,3));
        intervals.add(new Interval(1,1));
        intervals.add(new Interval(3,4));
        intervals.add(new Interval(8,11));
        intervals.add(new Interval(5,7));
        intervals.add(new Interval(7,8));

        intervals.add(new Interval(16,17));
        intervals.add(new Interval(12,16));
        intervals.add(new Interval(13,15));
        intervals.add(new Interval(9,11));
        intervals.add(new Interval(12,14));

        Collections.sort(intervals);

        Interval merge = Interval.copy(intervals.get(0));

        for (int i=1;i<intervals.size();i++)
        {
            Interval curr = intervals.get(i);


            if (inBetween(curr.left,merge.left,merge.right))
            {
                merge.right = Math.max(curr.right,merge.right);
            }
            else
            {
                merged.add(merge);
                merge = Interval.copy(curr);
            }
        }

        merged.add(merge);


        return merged;

    }


    private static boolean inBetween(int x1, int left , int right)
    {
        if (left<=x1 && x1<=right)
            return true ;
        else
            return false;
    }


    public static void main(String[] args) {

        System.out.println(test1());
    }


    public static List<Interval> test() {

        List<Interval> intervals = new ArrayList<>();

        List<Interval> merged = new ArrayList<>();

        intervals.add(new Interval(-4,-1));
        intervals.add(new Interval(0,2));
        intervals.add(new Interval(3,6));
        intervals.add(new Interval(7,9));
        intervals.add(new Interval(11,12));
        intervals.add(new Interval(14,17));

        Interval interval = new Interval(1,8);

        int left=Integer.MAX_VALUE, right=Integer.MIN_VALUE;




        // is it at the beginning

        if (interval.right<intervals.get(0).left)
        {
            merged.add(interval);
            merged.addAll(intervals);

            return merged;
        }

        // is it at the end
        if (interval.left > intervals.get(intervals.size()-1).right)
        {
            merged.addAll(intervals);
            merged.add(interval);

            return merged;
        }


        boolean added = false;
        for (int i=0;i<intervals.size();i++)
        {

            Interval curr = intervals.get(i);
            if (curr.right <= interval.left)
            {

                merged.add(curr);
                continue;
            }

            if (curr.left >= interval.right)
            {
                if (!added) {
                    merged.add(new Interval(left, right));
                    added=true;
                }


                merged.add(curr);
                continue;
            }

            // intersects

                left = Math.min(left,Math.min(curr.left,interval.left));
                right = Math.max(right,Math.max(curr.right,interval.right));



        }

        return merged;
    }

}
