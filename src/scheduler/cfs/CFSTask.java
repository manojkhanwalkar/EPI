package scheduler.cfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static scheduler.cfs.CFSScheduler.TimeSlice;

public class CFSTask implements Comparable<CFSTask> {


    final int id;
    public int preemptedCount=0;

    Set<Integer> preEmptedByTaskIds = new HashSet<>();

    volatile boolean prerempted = false;
    int priority;

    volatile int runTimeLeft = 2000;

    int remainingTimeSlice = TimeSlice;

    volatile int vruntime=0;

   volatile boolean completed = false;

   CFSScheduler CFSScheduler;

   Set<CFSScheduler>  schedulers = new HashSet<>();

    public CFSTask(int id, int remainingTimeSlice, int priority)
    {
        this.id = id;
        this.priority = priority;

    }


    @Override
    public int compareTo(CFSTask CFSTask) {


        int res = Integer.compare(this.vruntime, CFSTask.vruntime);

        if (res==0)
        {
            res=  Integer.compare(this.priority,CFSTask.priority);
        }
        if (res==0)
        {
            res = Integer.compare(this.id,CFSTask.id);
        }

        return res;

    }

    public void run()
    {

     //   System.out.println("CFSTask started " + id);
        long start = System.currentTimeMillis();

        while(!prerempted && System.currentTimeMillis()-start<remainingTimeSlice)
        {
            try {
                Thread.sleep(5);   // just to avoid cpu spin
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        long end = System.currentTimeMillis();

        int timeConsumedThisRun = (int)(end-start);
        runTimeLeft = runTimeLeft-timeConsumedThisRun;

        vruntime +=  priority*timeConsumedThisRun;

        CFSScheduler.lock.lock();

        if (prerempted)
        {
            System.out.println("CFSTask preempted " + this);

            remainingTimeSlice = remainingTimeSlice-timeConsumedThisRun;

            prerempted = false;

            CFSScheduler.current.add(this);
         //   CFSScheduler.totalTasksCount.incrementAndGet();

            // put back task in queue
        }
        else // time slice completed
        {
            if (runTimeLeft <= 0) {
                completed = true;   // remove task if completed .
                System.out.println("CFSTask completed " + this);
                CFSScheduler.totalTasksCount.decrementAndGet();
            } else {

                // System.out.println("Time slice expired " + this);

                CFSScheduler.expire(this);

            }
        }

          //  CFSScheduler.runningCFSTasks.remove(this);
            CFSScheduler.runningTasksCount.getAndDecrement();
            CFSScheduler.tasksToSchedule.signalAll();

            CFSScheduler.lock.unlock();


    }


    @Override
    public String toString() {
        return "CFSTask{" +
                "id=" + id +
                ", preemptedCount=" + preemptedCount +
                ", priority=" + priority +
                ", runTimeLeft=" + runTimeLeft +
                ", vruntime=" + vruntime +
                ", schedulers=" + schedulers +
                '}';
    }
}
