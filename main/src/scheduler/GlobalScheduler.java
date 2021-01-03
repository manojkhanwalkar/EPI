package scheduler;

import io.netty.handler.traffic.GlobalChannelTrafficCounter;
import scheduler.O1Scheduler.Scheduler;
import scheduler.cfs.CFSScheduler;
import scheduler.cfs.CFSTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class GlobalScheduler {


    int numCPU;

    List<CFSScheduler> schedulers ;

    TaskMigrationManager taskMigrationManager;

    public GlobalScheduler(int numCPU)
    {
        schedulers = new ArrayList<>(numCPU);
        this.numCPU = numCPU;

        for (int i=0;i<numCPU;i++)
        {
            schedulers.add(new CFSScheduler());
        }

        taskMigrationManager = new TaskMigrationManager(schedulers);

        new Thread(taskMigrationManager).start();
    }

    int next=0;



    public void add(CFSTask task)
    {
       // schedulers.get(next++%numCPU).add(task);

       schedulers.get(0).add(task);

    }



    static class TaskMigrationManager implements  Runnable
    {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<CFSScheduler> schedulers;
        public TaskMigrationManager( List<CFSScheduler> schedulers )
        {
            this.schedulers = schedulers;
        }

        public void run()
        {
            while(true)
            {
                int totalCount=0;

                for (CFSScheduler scheduler : schedulers)
                {
                    scheduler.lock();
                    totalCount+=scheduler.getTotalTasksCount();

                }

                int avg = totalCount/schedulers.size();

                List<CFSScheduler> toScheduler = new ArrayList<>();

                List<CFSTask> tasksToDistribute = new ArrayList<>();

                for (CFSScheduler scheduler : schedulers)
                {

                    int schedulerTaskCount = scheduler.getTotalTasksCount();
                    if (schedulerTaskCount==avg)
                        continue;
                    if (schedulerTaskCount>avg)
                    {
                        for (int i=0;i<schedulerTaskCount-avg;i++)
                        {
                            Optional<CFSTask> task =scheduler.migrate();
                            if (task.isEmpty())
                                break;
                            else
                                tasksToDistribute.add(task.get());
                            // get extra tasks from scheduler
                        }
                    }
                    else
                    {
                        toScheduler.add(scheduler);
                    }

                }

                int count = 0;
                for (int i=0;i<toScheduler.size();i++)
                {
                    if (tasksToDistribute.isEmpty())
                        break;
                    CFSScheduler scheduler = toScheduler.get(0);
                    int schedulerTaskCount = scheduler.getTotalTasksCount();

                    for (int j=0;j<avg-schedulerTaskCount;j++)
                    {
                        if (!tasksToDistribute.isEmpty())
                        {
                            scheduler.add(tasksToDistribute.remove(0));
                            count++;
                        }
                        else
                        {
                            break;
                        }
                    }

                }

                // if any tasks remaining - distribute them to scheduler 0

                while(!tasksToDistribute.isEmpty())
                {
                    schedulers.get(random.nextInt(schedulers.size())).add(tasksToDistribute.remove(0));
                }



                for (CFSScheduler scheduler : schedulers)
                {
                    scheduler.signal();
                    scheduler.unlock();

                }

           //     System.out.println("Total tasks pending to be scheduled are  " + totalCount + ", total tasks migrated are " + count);


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
