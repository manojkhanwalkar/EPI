package loadbalancer;

public interface Loadbalancer {

     void register(Service service);
    void deregister(Service service);
    ServiceProxy get();


}
