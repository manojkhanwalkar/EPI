package loadbalancer;

public class ServiceProxy {


    Service service;
    Counter counter;

    public void process()
    {
        counter.increment(service);
        service.process();
        counter.decrement(service);
    }

    private ServiceProxy(Service service, Counter counter) {
        this.service = service;
        this.counter = counter;
    }

    public static ServiceProxy create(Service service, Counter counter)
    {
        return new ServiceProxy(service,counter);
    }
}
