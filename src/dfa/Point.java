package dfa;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class Point {

    static Transition transition = new Transition("Point");

    static {

        transition.add("Love","S" , "15-0");
        transition.add("15-0","S" , "30-0");
        transition.add("30-0","S" , "40-0");
        transition.add("40-0","S" , "WIN-S");


        transition.add("Love","A" , "0-15");
        transition.add("0-15","A" , "0-30");
        transition.add("0-30","A" , "0-40");
        transition.add("0-40","A" , "WIN-A");

        transition.add("15-0","A" , "15-15");
        transition.add("0-15","S" , "15-15");

        transition.add("15-15","A" , "15-30");
        transition.add("15-15","S" , "30-15");

        transition.add("15-30","A" , "15-40");
        transition.add("15-30","S" , "30-30");

        transition.add("0-30","S" , "15-40");
        transition.add("30-0","A" , "30-15");

        transition.add("30-15","A" , "30-30");
        transition.add("30-15","S" , "40-15");

        transition.add("15-40","A" , "WIN-A");
        transition.add("15-40","S" , "30-40");

        transition.add("30-30","S" , "40-30");
        transition.add("30-30","A" , "30-40");

        transition.add("30-40","A" , "WIN-A");
        transition.add("30-40","S" , "DEUCE");


        transition.add("DEUCE","S" , "ADV-S");
        transition.add("DEUCE","A" , "ADV-A");
        transition.add("ADV-A","S" , "DEUCE");
        transition.add("ADV-S","A" , "DEUCE");


        transition.add("ADV-A","A" , "WIN-A");
        transition.add("ADV-S","S" , "WIN-S");


        transition.add("40-15","S" , "WIN-S");
        transition.add("40-15","A" , "40-30");

        transition.add("40-30","A" , "DEUCE");
        transition.add("40-30","S" , "WIN-S");

        transition.add("40-0","A" , "40-15");
        transition.add("0-40","S" , "15-40");


    }

    ThreadLocalRandom random = ThreadLocalRandom.current();

    String[] players = {"S" , "A"};
    public String play()
    {
        int index = random.nextInt(2);
        String player = players[index];

        String currentState = "Love";

        while(true)
        {
            //System.out.println("Current State is " + currentState);

           // System.out.println("Player " + player);


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String nextState = transition.get(currentState,player);
            if (nextState.startsWith("WIN"))
            {
                if (nextState.contains("S"))
                    return "S";
                else
                    return "A";
            }

            index = random.nextInt(2);
            player = players[index];
            currentState=nextState;
        }



    }

}
