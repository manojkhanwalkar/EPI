package dfa;

import java.util.concurrent.ThreadLocalRandom;

public class TieBreaker {


    static Transition transition = new Transition("Point");

    static {

        transition.add("0-0", "S", "1-0");
        transition.add("0-0", "A", "0-1");
        transition.add("1-0", "S", "WIN-S");
        transition.add("1-0", "A", "0-0");
        transition.add("0-1", "A", "WIN-A");
        transition.add("0-1", "S", "0-0");

    }


    ThreadLocalRandom random = ThreadLocalRandom.current();

    String[] players = {"S" , "A"};


    public String play()
    {
        int index = random.nextInt(2);
        String player = players[index];

        String currentState = "0-0";



            while(true)
            {
                System.out.println("Current State is " + currentState);

                // System.out.println("Player " + player);


                try {
                    Thread.sleep(100);
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
