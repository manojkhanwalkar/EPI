package scheduler.mflq;

import scheduler.cfs.CFSScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static scheduler.cfs.CFSScheduler.TimeSlice;

public class MFLQTask implements Comparable<MFLQTask> {


    final int id;
    public int preemptedCount=0;


    volatile boolean prerempted = false;

    int priority;

    volatile int runTimeLeft = 2000;

    int remainingTimeSlice = TimeSlice;


   volatile boolean completed = false;

   MFLQScheduler scheduler;

   Set<MFLQScheduler>  schedulers = new HashSet<>();

    public MFLQTask(int id, int priority)
    {
        this.id = id;
        this.priority = priority;

    }


    @Override
    public int compareTo(MFLQTask otherTask) {

        return Integer.compare(this.priority,otherTask.priority);

    }

    ThreadLocalRandom random = ThreadLocalRandom.current();

    public void run()
    {

     //   System.out.println("CFSTask started " + id);
        long start = System.currentTimeMillis();

        while(!prerempted && System.currentTimeMillis()-start<remainingTimeSlice)
        {
            try {
                Thread.sleep(5);   // just to avoid cpu spin

               int toss =  random.nextInt(2);

               if (toss==0)
                   prerempted = true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        long end = System.currentTimeMillis();

        int timeConsumedThisRun = (int)(end-start);
        runTimeLeft = runTimeLeft-timeConsumedThisRun;


        scheduler.lock.lock();

        if (prerempted)
        {
            //System.out.println("Task preempted " + this);

            remainingTimeSlice = remainingTimeSlice-timeConsumedThisRun;

            prerempted = false;


            scheduler.addToQueue(this);


            // put back task in queue
        }
        else // time slice completed
        {
            if (runTimeLeft <= 0) {
                completed = true;   // remove task if completed .
                System.out.println("Task completed " + this);
                scheduler.totalTasksCount.decrementAndGet();
            } else {

                // System.out.println("Time slice expired " + this);

                scheduler.expire(this);

            }
        }

          //  CFSScheduler.runningCFSTasks.remove(this);
            scheduler.runningTasksCount.getAndDecrement();
            scheduler.tasksToSchedule.signalAll();

            scheduler.lock.unlock();


    }

    @Override
    public String toString() {
        return "MFLQTask{" +
                "id=" + id +
                ", preemptedCount=" + preemptedCount +
                ", prerempted=" + prerempted +
                ", priority=" + priority +
                ", runTimeLeft=" + runTimeLeft +
                ", remainingTimeSlice=" + remainingTimeSlice +
                ", completed=" + completed +
                ", scheduler=" + scheduler +
                ", schedulers=" + schedulers +
                '}';
    }
}
