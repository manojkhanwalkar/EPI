package loadbalancer;

import java.util.concurrent.CompletableFuture;

public class LBTester {

    public static void main(String[] args) throws Exception {
//        Loadbalancer loadbalancer = new RRLoadbalancer();
 //       Loadbalancer loadbalancer = new LeastLoadedLoadbalancer();
        Loadbalancer loadbalancer = new WeightedLeastLoadedLoadbalancer();


        test(loadbalancer);

        while(true) Thread.sleep(1000);
    }

    public static void test(Loadbalancer loadbalancer)
    {



        for (int i=0;i<3;i++)
        {
            Service service = new Service();
            ((WeightedLeastLoadedLoadbalancer)loadbalancer).registerWithWeight(service, i*10);
        }

        for (int i=0;i<5;i++)
        {
            CompletableFuture.runAsync(()->{

                for (int j=0;j<10;j++) {
                    loadbalancer.get().process();
                }

            });
        }



    }
}
