package scheduler;

import scheduler.cfs.CFSTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static scheduler.cfs.CFSTasksArray.PriorityLevels;

public class SchedulerTester {


    static class CFSStatus implements Runnable
    {

        List<CFSTask> cfsTasks;

        public CFSStatus(List<CFSTask> cfsTasks)
        {
            this.cfsTasks  = cfsTasks;
        }

        public void run()
        {
            System.out.println("Running shutdown hook\n\n\n");

            cfsTasks.stream().filter(t->t.preemptedCount==0).forEach(System.out::println);
        }
    }

    public static void main(String[] args) throws Exception  {


        GlobalScheduler scheduler = new GlobalScheduler(5);


        Random random = new Random();

        List<CFSTask> cfsTasks = new ArrayList<>();

        //  while(true) {

        for (int i = 0; i < 1000; i++) {
            //CFSTask task = new CFSTask(random.nextInt(Integer.MAX_VALUE), 100, random.nextInt(PriorityLevels));
            CFSTask task = new CFSTask(i, 100, i);
            scheduler.add(task);

            cfsTasks.add(task);

        //   Thread.sleep(100);

        }
        Runtime.getRuntime().addShutdownHook(new Thread(new CFSStatus(cfsTasks)));


    }
}
