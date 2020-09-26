package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MinimumWaitingTime {


    public static void main(String[] args) {

        List<Integer> timings = new ArrayList<>();

        Random random = new Random();

        for (int i=0;i<5;i++)
            timings.add(random.nextInt(5));


        Collections.sort(timings);

        int waitTime=0;
        for (int i=1;i<timings.size();i++)
        {
            waitTime = waitTime+timings.get(i-1);
        }

        System.out.println(timings);

        System.out.println(waitTime);



    }
}
