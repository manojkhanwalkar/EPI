package dfa;

public class Match {

    int s;
    int a;

    public void play()
    {
        while(true) {
            Set set = new Set();

            String player = set.play();

            int winner ;


            if (player.equals("S")) {
                s++;
                winner = s;

            }
            else {

                a++;
                winner = a;

            }


            System.out.println("Winner of set is " + player + " Score is S-A " + s + "-" + a);

            if (winner==3)
            {
                System.out.println("Match goes to " + player);
                return;
            }



        }
    }


    public static void main(String[] args) {

        Match match = new Match();

        match.play();
    }

}
