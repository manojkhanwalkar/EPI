package fargate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessHealthWatcher {


    ServiceCoordinator serviceCoordinator;

    ExecutorService service ;

    public ProcessHealthWatcher( ServiceCoordinator serviceCoordinator)
    {

        this.serviceCoordinator = serviceCoordinator;


        service = Executors.newSingleThreadExecutor(r -> {
            final Thread thread = new Thread(r);
            thread.setDaemon(false); //change me
            return thread;
        });

        init();
    }

    private void init()
    {
        service.submit(()->{

            while(true)
            {
                serviceCoordinator.tasks.values().forEach(task->{
                    if (!task.getProcess().isAlive())
                    {
                        try {
                            serviceCoordinator.restart(task);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

                Thread.sleep(10000);
            }


        });
    }

}
