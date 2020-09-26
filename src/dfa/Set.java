package dfa;

public class Set {

    int s;
    int a;



    public String play()
    {
        while(true) {
            Point point = new Point();

            String player = point.play();


            System.out.println("Winner of point is " + player);
            System.out.println("Points for the set are " + s + " " + a);

            int winner ; int other;

            if (player.equals("S"))
            {
                s++;
                winner = s;
                other=a;
            }
            else
            {
                a++;
                winner = a;
                other = s;
            }

            switch(winner)
            {
                case 7 :
                    return player;
                case 6:
                    if (winner-other>=2)
                        return player;
                    if(winner==other) {
                        return new TieBreaker().play();
                    }

            }

            // in other cases continue the play.


        }

    }

}
