

package loadbalancer;

        import java.util.Comparator;
        import java.util.concurrent.ConcurrentHashMap;
        import java.util.concurrent.ConcurrentMap;
        import java.util.concurrent.atomic.AtomicInteger;

public class WeightedLeastLoadedLoadbalancer implements Loadbalancer, Counter {

    public static int DefaultWeight = 5;   // 1 is the max weight and 10 is the lowest weight.
    ConcurrentMap<Service, AtomicInteger> serviceUsage = new ConcurrentHashMap<>();

    ConcurrentMap<Service,Integer> weights = new ConcurrentHashMap<>();

    @Override
    public void register(Service service) {
        serviceUsage.put(service,new AtomicInteger(0));
        weights.put(service,DefaultWeight);
    }

    public void registerWithWeight(Service service, int weight)
    {
        serviceUsage.put(service,new AtomicInteger(0));
        weights.put(service,weight);
    }


    @Override
    public void deregister(Service service) {

        serviceUsage.remove(service);
        weights.remove(service);
    }

    @Override
    public ServiceProxy get() {
        var service = serviceUsage.entrySet().stream().sorted(Comparator.comparing(e->{
            var count = e.getValue().get();
            var weight = weights.get(e.getKey());
            return count*weight;
        })).findFirst().get().getKey();
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

