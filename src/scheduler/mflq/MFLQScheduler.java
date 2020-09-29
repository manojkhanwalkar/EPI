package scheduler.mflq;

import scheduler.cfs.CFSTask;
import scheduler.cfs.CFSTasksArray;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MFLQScheduler {

    public static final int PriorityLevel1 = 100;

    public static final int PriorityLevel2 = 200;

    public static final int PriorityLevel3 = 300;


    PriorityQueue<MFLQTask> level1 = new PriorityQueue<>();

    PriorityQueue<MFLQTask> level2= new PriorityQueue<>();

    PriorityQueue<MFLQTask> level3 = new PriorityQueue<>();



    Thread schedulingThread ;
    TaskSchedulingManager taskSchedulingManager ;

    Lock lock = new ReentrantLock();

    Condition tasksToSchedule;

   // PriorityQueue<CFSTask> runningCFSTasks = new PriorityQueue<>(Collections.reverseOrder());


    public MFLQScheduler()
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


    public void add(MFLQTask t)
    {
        try {
            lock.lock();

            totalTasksCount.incrementAndGet();

            addToQueue(t);

        } finally {
            lock.unlock();
        }
    }

    protected void addToQueue(MFLQTask t)
    {
        if (!directlyScheduled(t)) {
            if (t.priority<PriorityLevel1)
            {
                level1.add(t);
            }
            else if (t.priority<PriorityLevel2)
            {
                level2.add(t);
            } else if (t.priority<PriorityLevel3) {
                level3.add(t);
            }

            tasksToSchedule.signalAll();
        }
    }

    static final int TimeSlice=100;

    ThreadLocalRandom random = ThreadLocalRandom.current();

    public void expire(MFLQTask t)
    {
        try {
            lock.lock();
                t.remainingTimeSlice=TimeSlice;
                t.preemptedCount++;
               // expired.add(t);
                if (t.priority <PriorityLevel1)
                {
                    t.priority = PriorityLevel1 + random.nextInt(PriorityLevel2-PriorityLevel1);
                } else if (t.priority < PriorityLevel2)
                {
                    t.priority = PriorityLevel2 + random.nextInt(PriorityLevel3-PriorityLevel2);
                }
            addToQueue(t);

        } finally {
            lock.unlock();
        }
    }



    static int MAXTHREADS =5;

    AtomicInteger runningTasksCount = new AtomicInteger(0);

    private   boolean directlyScheduled(MFLQTask task) {
        try {
            lock.lock();
            // check if there is a thread free for running .
            int num = runningTasksCount.get();
            if (num < MAXTHREADS) {
                runTask(task);
                return true;
            }


            return false;

        }finally {

            lock.unlock();

        }
    }

    protected Optional<MFLQTask> get()
    {
        // try level1 ... 3

        Optional<MFLQTask> task ;

        if (!level1.isEmpty()) {
            task = Optional.of(level1.remove());
        }
        else if (!level2.isEmpty())
        {
            task = Optional.of(level2.remove());
        } else if (!level3.isEmpty())
        {
            task = Optional.of(level3.remove());
        } else
        {
            task = Optional.empty();
        }

       return task;
    }



    protected void runTask(MFLQTask task)
    {
        runningTasksCount.getAndIncrement();
        task.scheduler = this;

        task.schedulers.add(this);
      //  runningCFSTasks.add(CFSTask);
        CompletableFuture.runAsync(()-> task.run());

    }




    static class TaskSchedulingManager implements  Runnable
    {

        MFLQScheduler scheduler;
        public TaskSchedulingManager (MFLQScheduler scheduler)
        {
            this.scheduler = scheduler;
        }

        public void run()
        {
            while(true)
            {
                try {
                    scheduler.lock.lock();


                    Optional<MFLQTask> task = scheduler.get();
                    while (task.isEmpty())
                    {
                        scheduler.tasksToSchedule.await();
                        task = scheduler.get();

                    }

                    int num = scheduler.runningTasksCount.get();
                    while (num>=MAXTHREADS) {
                        scheduler.tasksToSchedule.await(1000, TimeUnit.MILLISECONDS);
                        num = scheduler.runningTasksCount.get();
                    }


                    // valid task found - run it on an available thread.

                    MFLQTask t = task.get();

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
