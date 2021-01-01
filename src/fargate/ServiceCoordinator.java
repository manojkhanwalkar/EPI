package fargate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceCoordinator {

    ConcurrentMap<String,TaskDef> tasks = new ConcurrentHashMap<>();


    TaskDefWatcher watcher ;
    ProcessHealthWatcher processHealthWatcher;
    public ServiceCoordinator()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHook(this)));
        watcher = new TaskDefWatcher("/home/manoj/data/svccoordinator/",this);
        processHealthWatcher = new ProcessHealthWatcher(this);
    }

    public synchronized void register(TaskDef taskDef) throws Exception {
        // if task exists , delete that process and then start this one.
        var task = tasks.get(taskDef.getServiceName());
        if (task!=null)
        {
           // task.getProcess().destroy();
            Runtime.getRuntime().exec("kill -SIGINT " + task.getProcess().pid());
        }

        tasks.put(taskDef.getServiceName(),taskDef);
        Process p = process(taskDef.getCommand());
        taskDef.setProcess(p);


    }

    public synchronized void restart(TaskDef taskDef) throws Exception
    {
        if (taskDef.getProcess().isAlive())  // for a race condition where registering a new task def has stopped an earlier process.
            return ;
        Process p = process(taskDef.getCommand());
        taskDef.setProcess(p);
    }

   /* public static void main(String[] args) throws Exception {

        while(true)
        {
            Process p = process();
            while(p.isAlive())
            {
                Thread.sleep(1000);
            }

        }

    }*/

    public static Process process(String command) throws Exception  {

        Runtime r = Runtime.getRuntime();
        Process p = r.exec(command);
        System.out.println("Starting process " + p.pid());
        System.out.println(p.info());


/*


        CompletableFuture.runAsync(()->{

            try {
                p.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);

                Runtime.getRuntime().exec("kill -SIGINT " + p.pid());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //p.destroy();

        });


 */

//TODO - change to writing to a file
        StreamProcessor streamProcessor = new StreamProcessor(p.getInputStream(),System.out::println);

        CompletableFuture.runAsync(streamProcessor);

        return p;


    }
}