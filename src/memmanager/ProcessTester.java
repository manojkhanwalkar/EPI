package memmanager;

import java.util.concurrent.CompletableFuture;

public class ProcessTester {

    //TODO - make it multi thread safe so that process can be run in parallel .


    public static void main(String[] args) {


        Process process = new Process("PROC1", 'a');
        Process process1 = new Process("PROC2",'A');
        Process process2 = new Process("PROC2",'W');

        CompletableFuture cf1 = CompletableFuture.runAsync(()->{

            process.run();

        });

        CompletableFuture cf2 = CompletableFuture.runAsync(()->{

            process1.run();

        });

        CompletableFuture cf3 = CompletableFuture.runAsync(()->{

            process2.run();

        });

        CompletableFuture.allOf(cf1,cf2,cf3).join();







    }
}
