package memmanager;

public class ProcessTester {

    //TODO - make it multi thread safe so that process can be run in parallel .


    public static void main(String[] args) {

        Process process = new Process("PROC1", 'a');

        process.run();

     Process process1 = new Process("PROC2",'A');

       process1.run();

        Process process2 = new Process("PROC2",'W');

        process2.run();



    }
}
