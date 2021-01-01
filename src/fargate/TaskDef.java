package fargate;

public class TaskDef {
    //"java -cp  /home/manoj/IdeaProjects/JWT/target/JWT-1.0-SNAPSHOT.jar services.authorizer.AuthApplication server /home/manoj/IdeaProjects/JWT/src/main/resources/Auth.yml " ;
    // "java -cp /home/manoj/IdeaProjects/EPI/target/EPI-1.0-SNAPSHOT.jar fargate.DemoClient" ;

    String command ;
    String serviceName;

    Process process;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
