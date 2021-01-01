package fargate;

import org.junit.jupiter.api.Test;

public class CoordinatorTest {

    @Test
    public void test() throws Exception
    {
        ServiceCoordinator coordinator = new ServiceCoordinator();

        {
            TaskDef taskDef = new TaskDef();
            taskDef.setServiceName("Demo");
            taskDef.setCommand("java -cp /home/manoj/IdeaProjects/EPI/target/EPI-1.0-SNAPSHOT.jar fargate.DemoClient");
            coordinator.register(taskDef);

        }

        {
            TaskDef taskDef = new TaskDef();
            taskDef.setServiceName("JWT");
            taskDef.setCommand("java -cp  /home/manoj/IdeaProjects/JWT/target/JWT-1.0-SNAPSHOT.jar services.authorizer.AuthApplication server /home/manoj/IdeaProjects/JWT/src/main/resources/Auth.yml ");
            coordinator.register(taskDef);

        }

        Thread.sleep(10000);
    }
}
