package loadbalancer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RRLoadbalancer implements Loadbalancer , Counter{

    List<Service> services = new CopyOnWriteArrayList<>();

    @Override
    public void register(Service service) {
        services.add(service);
    }

    @Override
    public void deregister(Service service) {
        services.remove(service);
    }

    AtomicInteger count = new AtomicInteger(0);
    @Override
    public ServiceProxy get() {

        int next = count.getAndIncrement();
        var serviceList = services;
        return ServiceProxy.create(serviceList.get(next%serviceList.size()),this);
    }
}
