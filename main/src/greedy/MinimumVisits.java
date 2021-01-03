package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinimumVisits {


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
            return Integer.compare(right,interval.right);
        }

        public static Interval copy(Interval interval)
        {
            Interval newInterval = new Interval(interval.left,interval.right);
            return newInterval;
        }
    }


    public static void main(String[] args) {


        List<Interval> intervals = new ArrayList<>();

        intervals.add(new Interval(1,2));
        intervals.add(new Interval(2,3));
        intervals.add(new Interval(3,4));
        intervals.add(new Interval(2,3));
        intervals.add(new Interval(3,4));
        intervals.add(new Interval(5,5));

        Collections.sort(intervals);
        List<Integer> times = new ArrayList<>();

        int time=0;
        for (int i=0;i<intervals.size();i++)
        {
            Interval curr = intervals.get(i);


                if (inBetween(time,curr))
                {
                    // do nothing
                }
                else
                {
                    time = curr.right;
                    times.add(time);
                }

        }

        System.out.println(times);

    }

    private static boolean inBetween(int time, Interval curr) {

        if (curr.left<= time && curr.right>= time)
            return true;
        else
            return false;
    }


}
