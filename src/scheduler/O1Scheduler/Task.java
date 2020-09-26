package scheduler.O1Scheduler;

import java.util.HashSet;
import java.util.Set;

import static scheduler.O1Scheduler.Scheduler.TimeSlice;

public class Task implements Comparable<Task> {


    final int id;
    public int preemptedCount=0;

    Set<Integer> preEmptedByTaskIds = new HashSet<>();

    volatile boolean prerempted = false;
    int priority;

    int runTimeLeft = 2000;

    int remainingTimeSlice = TimeSlice;

   volatile boolean completed = false;

   Scheduler scheduler;

    public Task(int id, int remainingTimeSlice, int priority)
    {
        this.id = id;
        this.priority = priority;

    }


    @Override
    public int compareTo(Task task) {
        return Integer.compare(this.priority,task.priority);
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

        runTimeLeft = runTimeLeft-(int)(end-start);
        scheduler.lock.lock();

        if (prerempted)
        {
            System.out.println("CFSTask preempted " + this);

            remainingTimeSlice = remainingTimeSlice-(int)(end-start);

            prerempted = false;

            scheduler.current.add(this);


            // put back task in queue
        }
        else // time slice completed
        {
            if (runTimeLeft <= 0) {
                completed = true;   // remove task if completed .
                System.out.println("CFSTask completed " + this);
            } else {

                // System.out.println("Time slice expired " + this);

                scheduler.expire(this);

            }
        }

            scheduler.runningTasks.remove(this);
            scheduler.runningTasksCount.getAndDecrement();
            scheduler.tasksToSchedule.signalAll();

            scheduler.lock.unlock();


    }


    @Override
    public String toString() {
        return "CFSTask{" +
                "id=" + id +
                ", preemptedCount=" + preemptedCount +
                ", preEmptedByTaskIds=" + preEmptedByTaskIds +
                ", prerempted=" + prerempted +
                ", priority=" + priority +
                ", runTimeLeft=" + runTimeLeft +
                ", remainingTimeSlice=" + remainingTimeSlice +
                ", completed=" + completed +

                '}';
    }
}
