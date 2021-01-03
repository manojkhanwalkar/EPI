package fargate;

import java.io.IOException;

public class ShutDownHook implements Runnable {

    ServiceCoordinator serviceCoordinator;
    public ShutDownHook(ServiceCoordinator serviceCoordinator)
    {
        this.serviceCoordinator = serviceCoordinator;
    }

    public void run()
    {
        serviceCoordinator.tasks.values().forEach(task->{

            System.out.println("Shutting down " + task.getProcess().pid());

            try {
                Runtime.getRuntime().exec("kill -SIGINT " + task.getProcess().pid());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //task.getProcess().destroy();

        });
    }
}
