package timers;

import io.dropwizard.servlets.tasks.Task;

import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduledTimer {

    final PriorityQueue<TaskHandle> tasks = new PriorityQueue<>();

    Lock lock = new ReentrantLock();
    Condition taskAdded = lock.newCondition();

    public ScheduledTimer()
    {
        TaskRunner taskRunner = new TaskRunner(this);

        Thread t = new Thread(taskRunner);
        t.start();
    }




public TaskHandle add(int initialDelay, int freq , Runnable task) {
        TaskHandle taskHandle = new TaskHandle(task,initialDelay,freq);

        taskHandle.nextScheduledTime = System.currentTimeMillis()+initialDelay;

        lock.lock();
         tasks.add(taskHandle);
         taskAdded.signalAll();
         lock.unlock();

        return taskHandle;
}



static class TaskRunner  implements Runnable
{
    final PriorityQueue<TaskHandle> tasks;
    final ScheduledTimer scheduledTimer;

    public TaskRunner( ScheduledTimer scheduledTimer )
    {
        this.tasks=scheduledTimer.tasks;
        this.scheduledTimer=scheduledTimer;
    }


    public void run()
    {
        try {
            while(true)
            {
                scheduledTimer.lock.lock();
                if (tasks.isEmpty())
                {
                    scheduledTimer.taskAdded.await();
                }

                // not empty check if first task is due to run .
                long curr = System.currentTimeMillis();
                TaskHandle handle = tasks.peek();
                while (handle!=null && curr >= handle.nextScheduledTime)
                {
                    handle = tasks.remove();

                    if (!handle.cancelled) {
                        // TODO run the task in a separate thread.
                        CompletableFuture.runAsync(handle.task);

                        handle.nextScheduledTime = curr + handle.freq;
                        tasks.add(handle);
                    }
                    handle = tasks.peek();
                }

                if (handle!=null) {
                    long sleepTime = handle.nextScheduledTime - curr;

                    scheduledTimer.taskAdded.await(sleepTime, TimeUnit.MILLISECONDS);
                }

                scheduledTimer.lock.unlock();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


}
