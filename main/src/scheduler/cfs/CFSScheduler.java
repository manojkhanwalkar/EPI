package scheduler.cfs;

import java.util.Collections;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CFSScheduler {

    volatile CFSTasksArray current = new CFSTasksArray();

    Thread schedulingThread ;
    TaskSchedulingManager taskSchedulingManager ;

    Lock lock = new ReentrantLock();

    Condition tasksToSchedule;

   // PriorityQueue<CFSTask> runningCFSTasks = new PriorityQueue<>(Collections.reverseOrder());


    public CFSScheduler()
    {
        this.taskSchedulingManager = new TaskSchedulingManager(this);
        this.schedulingThread = new Thread(taskSchedulingManager);

        tasksToSchedule = lock.newCondition();

        schedulingThread.start();
    }


    public void lock()
    {
        lock.lock();
    }

    public void unlock()
    {
        lock.unlock();
    }

    public void signal()
    {
        tasksToSchedule.signalAll();
    }

    AtomicInteger totalTasksCount = new AtomicInteger(0);

    public int getTotalTasksCount() {
        return totalTasksCount.get();
    }

    public Optional<CFSTask> migrate()
    {
        try {
            lock();
            Optional<CFSTask> task = current.get();
            if (task.isPresent()) totalTasksCount.decrementAndGet();
            return task;
        } finally {
            unlock();
        }
    }

    public void add(CFSTask t)
    {
        try {
            lock.lock();

            totalTasksCount.incrementAndGet();

            if (!directlyScheduled(t)) {
                current.add(t);
                tasksToSchedule.signalAll();
            }

        } finally {
            lock.unlock();
        }
    }

    public static final int TimeSlice=100;

    public void expire(CFSTask t)
    {
        try {
            lock.lock();
                t.remainingTimeSlice=TimeSlice;
                t.preemptedCount++;
               // expired.add(t);
            current.add(t);

        } finally {
            lock.unlock();
        }
    }



    static int MAXTHREADS =5;

    AtomicInteger runningTasksCount = new AtomicInteger(0);

    private   boolean directlyScheduled(CFSTask CFSTask) {
        try {
            lock.lock();
            // check if there is a thread free for running .
            int num = runningTasksCount.get();
            if (num < MAXTHREADS) {
                runTask(CFSTask);
                return true;
            }

    /*        // if not check if there is a low priority CFSTask that can be preempted.
            if (!runningCFSTasks.isEmpty() && CFSTask.priority < runningCFSTasks.peek().priority) {
                // preempt that CFSTask.
                CFSTask toRemove = runningCFSTasks.peek();
                toRemove.prerempted = true;
                toRemove.preemptedCount++;
                toRemove.preEmptedByTaskIds.add(CFSTask.id);

            }*/

            return false;

        } catch (Exception e)
        {
            System.out.println( "Running tasks count  " + runningTasksCount.get() );
            e.printStackTrace();
            return false;

    }finally {

            lock.unlock();

        }
    }

    protected Optional<CFSTask> get()
    {
        return current.get();
    }

  /*  protected void switchTasks()
    {
        CFSTasksArray temp = current;
        current = expired;
        expired = temp;
    }*/


    protected void runTask(CFSTask CFSTask)
    {
        runningTasksCount.getAndIncrement();
        CFSTask.CFSScheduler = this;

        CFSTask.schedulers.add(this);
      //  runningCFSTasks.add(CFSTask);
        CompletableFuture.runAsync(()-> CFSTask.run());

    }




    static class TaskSchedulingManager implements  Runnable
    {

        CFSScheduler CFSScheduler;
        public TaskSchedulingManager (CFSScheduler CFSScheduler)
        {
            this.CFSScheduler = CFSScheduler;
        }

        public void run()
        {
            while(true)
            {
                try {
                    CFSScheduler.lock.lock();


                    Optional<CFSTask> task = CFSScheduler.get();
                    while (task.isEmpty())
                    {
                        CFSScheduler.tasksToSchedule.await();
                        task = CFSScheduler.get();

                    }

                    int num = CFSScheduler.runningTasksCount.get();
                    while (num>=MAXTHREADS) {
                        CFSScheduler.tasksToSchedule.await(1000, TimeUnit.MILLISECONDS);
                        num = CFSScheduler.runningTasksCount.get();
                    }


                    // valid task found - run it on an available thread.

                    CFSTask t = task.get();

                    CFSScheduler.runTask(t);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    CFSScheduler.lock.unlock();
                }
            }

        }
    }




}
