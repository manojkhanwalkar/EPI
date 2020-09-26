package java891011121314.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Cache implements Flow.Publisher<Context> {

    List<CacheSubscription> cacheSubscriptions = new ArrayList<>();

    @Override
    public void subscribe(Flow.Subscriber<? super Context> subscriber) {

        CacheSubscription subscription = new CacheSubscription(subscriber,this);
        cacheSubscriptions.add(subscription);

        subscriber.onSubscribe(subscription);

    }

    private void processSubscription(Object object, Context.Type type,Context.Operation operation)
    {
        Context context = new Context();

        context.object=object;
        context.type = type;
        context.operation=operation;
        cacheSubscriptions.stream().forEach(cacheSubscription -> { cacheSubscription.add(context);});
    }

    public void order(Order order)
    {
        processSubscription(order, Context.Type.Order, Context.Operation.Add);
    }


    public void execution(Execution execution)
    {

    }


    public void start()
    {

    }


    public void stop()
    {
        // send stop message to all subscrbers.
    }

    private Cache()
    {

    }

    public static Cache getInstance()
    {
        return CacheHolder.instance;
    }

    static class CacheHolder
    {
        static Cache instance = new Cache();
    }
}
