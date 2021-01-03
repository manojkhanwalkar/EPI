package aero;

import java.util.concurrent.CompletableFuture;

public class AeroNodeTester {

    public static void main(String[] args) {

        Node node = new Node();

        node.put("Hello1" , "World1");
        node.put("Hello2" , "World2");
        node.put("Hello3" , "World3");
        node.put("Hello4" , "World4");
        node.put("Hello5" , "World5");

        CompletableFuture[] cfs = new CompletableFuture[5];

        for (int i=0;i<5;i++) {

            int index = i;
            cfs[i] = CompletableFuture.runAsync(() -> {

                String str = node.get("Hello" + index);

                System.out.println(str);


            });

        }


        CompletableFuture.allOf(cfs).join();

        //node.delete("Hello");

      //  System.out.println(node.get("Hello1"));


        node.shutdown();
    }

}
