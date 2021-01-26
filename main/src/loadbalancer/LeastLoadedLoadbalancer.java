package loadbalancer;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LeastLoadedLoadbalancer implements Loadbalancer, Counter {

   ConcurrentMap<Service, AtomicInteger> serviceUsage = new ConcurrentHashMap<>();

    @Override
    public void register(Service service) {
        serviceUsage.put(service,new AtomicInteger(0));
    }

    @Override
    public void deregister(Service service) {

        serviceUsage.remove(service);
    }

    @Override
    public ServiceProxy get() {
        var service = serviceUsage.entrySet().stream().sorted(Comparator.comparing(e->e.getValue().get())).findFirst().get().getKey();
        return  ServiceProxy.create(service,this);
    }

    @Override
    public void increment(Service service) {

        this.serviceUsage.get(service).getAndIncrement();

    }

    @Override
    public void decrement(Service service) {
        this.serviceUsage.get(service).getAndDecrement();
    }
}
