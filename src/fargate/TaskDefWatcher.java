package fargate;

import java891011121314.JSONUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDefWatcher {

    String directoryToWatch ;
    ServiceCoordinator serviceCoordinator;

    ExecutorService service ;

    public TaskDefWatcher(String directoryToWatch,ServiceCoordinator serviceCoordinator)
    {
        this.directoryToWatch = directoryToWatch;
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

            Map<String,Long> files = new HashMap<>();

            while(true) {
                File file = new File(directoryToWatch);
                Arrays.stream(file.listFiles()).forEach(f->{


                    if (files.get(f.getName())==null || files.get(f.getName()) < f.lastModified())
                    {
                        files.put(f.getName(),f.lastModified());
                        try(BufferedReader reader = new BufferedReader(new FileReader(f)))
                        {
                            String str = reader.readLine();
                            TaskDef taskDef = (TaskDef) JSONUtil.fromJSON(str,TaskDef.class);
                            serviceCoordinator.register(taskDef);
                        }catch (Exception e) { e.printStackTrace();}

                    }


                });

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });  ;
    }
}
