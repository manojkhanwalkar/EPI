package scheduler.O1Scheduler;

import java.util.Collections;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {

    volatile TasksArray current = new TasksArray();
    volatile TasksArray expired = new TasksArray();

    Thread schedulingThread ;
    TaskSchedulingManager taskSchedulingManager ;

    Lock lock = new ReentrantLock();

    Condition tasksToSchedule;

    PriorityQueue<Task> runningTasks = new PriorityQueue<>(Collections.reverseOrder());


    public Scheduler()
    {
        this.taskSchedulingManager = new TaskSchedulingManager(this);
        this.schedulingThread = new Thread(taskSchedulingManager);

        tasksToSchedule = lock.newCondition();

        schedulingThread.start();
    }


    public void add(Task t)
    {
        try {
            lock.lock();

            if (!directlyScheduled(t)) {
                current.add(t);
                tasksToSchedule.signalAll();
            }

        } finally {
            lock.unlock();
        }
    }

    static final int TimeSlice=100;

    public void expire(Task t)
    {
        try {
            lock.lock();
                t.remainingTimeSlice=TimeSlice;
                expired.add(t);

        } finally {
            lock.unlock();
        }
    }



    static int MAXTHREADS =5;

    AtomicInteger runningTasksCount = new AtomicInteger(0);

    private   boolean directlyScheduled(Task task) {
        try {
            lock.lock();
            // check if there is a thread free for running .
            int num = runningTasksCount.get();
            if (num < MAXTHREADS) {
                runTask(task);
                return true;
            }

            // if not check if there is a low priority task that can be preempted.
            if (!runningTasks.isEmpty() && task.priority < runningTasks.peek().priority) {
                // preempt that task.
                Task toRemove = runningTasks.peek();
                toRemove.prerempted = true;
                toRemove.preemptedCount++;
                toRemove.preEmptedByTaskIds.add(task.id);

            }

            return false;

        } catch (Exception e)
        {
            System.out.println(runningTasks.size()  + "  " + runningTasksCount.get() );
            e.printStackTrace();
            return false;

    }finally {

            lock.unlock();

        }
    }

    protected Optional<Task> get()
    {
        return current.get();
    }

    protected void switchTasks()
    {
        TasksArray temp = current;
        current = expired;
        expired = temp;
    }


    protected void runTask(Task task)
    {
        runningTasksCount.getAndIncrement();
        task.scheduler = this;
        runningTasks.add(task);
        CompletableFuture.runAsync(()->task.run());

    }




    static class TaskSchedulingManager implements  Runnable
    {

        Scheduler scheduler ;
        public TaskSchedulingManager (Scheduler scheduler)
        {
            this.scheduler = scheduler;
        }

        public void run()
        {
            while(true)
            {
                try {
                    scheduler.lock.lock();

                    int num = scheduler.runningTasksCount.get();
                    if (num>=MAXTHREADS)
                        scheduler.tasksToSchedule.await();

                    Optional<Task> task = scheduler.get();
                    if (task.isEmpty())
                    {
                        scheduler.switchTasks();
                        task = scheduler.get();
                        if (task.isEmpty())
                        {
                            scheduler.tasksToSchedule.await();
                            continue;
                        }
                    }

                    // valid task found - run it on an available thread.

                    Task t = task.get();

                    scheduler.runTask(t);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    scheduler.lock.unlock();
                }
            }

        }
    }




}
