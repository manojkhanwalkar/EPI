package timers;

public class TaskHandle implements Comparable<TaskHandle>{

    volatile boolean cancelled = false;

    Runnable task;

    int initialDelay ;

    int freq;


    long nextScheduledTime;

    public void cancel()
    {
        cancelled = true;
    }


    protected TaskHandle(Runnable task, int initialDelay , int freq)
    {
        this.task = task;
        this.initialDelay = initialDelay;
        this.freq=freq;

    }


    protected void setNextScheduledTime(long time)
    {
        nextScheduledTime = time;
    }


    @Override
    public int compareTo(TaskHandle taskHandle) {

        return Long.compare(this.nextScheduledTime, taskHandle.nextScheduledTime);
    }




}
