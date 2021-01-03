package multiprocessor.work;

public class WSQRecursiveAction extends WSQRA {


    String created;
    String submitted;
    String processed;

    int number;

    public WSQRecursiveAction(int number)
    {
        created = Thread.currentThread().getName();
        this.number = number;
    }



    public void run()
    {


            if (number<1)
                return;

           // System.out.println("Running " + number);

            WSQRecursiveAction left = new WSQRecursiveAction(number/2);
            WSQRecursiveAction right = new WSQRecursiveAction(number/2);

            // no join for now

            left.fork();
            right.fork();


          System.out.println("forked " + number);


     /*   try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


      //   left.join();
        // right.join();


        System.out.println("Created by " + created +  " processed by " + Thread.currentThread().getName() +"    " + number);

      /*  try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
    }

}
