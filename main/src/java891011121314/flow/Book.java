package java891011121314.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Book implements Flow.Subscriber<Context>, Flow.Publisher<Context> {

    CacheSubscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

        this.subscription = (CacheSubscription) subscription;

        subscription.request(1);

    }

    @Override
    public void onNext(Context context) {

       // System.out.println(context);

        Order order = (Order)context.object;

        int len = order.orderId.length();
        String id = order.orderId.substring(len-1,len);

         Execution execution = new Execution("Exec"+id, order.orderId,order.qty,order.price);


        processSubscription(execution, Context.Type.Execution, Context.Operation.Add);

        subscription.request(1);


    }

    @Override
    public void onError(Throwable throwable) {

        throwable.printStackTrace();

        subscription.request(1);

    }

    @Override
    public void onComplete() {

    }


    List<BookSubscription> bookSubscriptions = new ArrayList<>();

    @Override
    public void subscribe(Flow.Subscriber<? super Context> subscriber) {

        BookSubscription subscription = new BookSubscription(subscriber,this);
        bookSubscriptions.add(subscription);

        subscriber.onSubscribe(subscription);

    }







    private void processSubscription(Object object, Context.Type type,Context.Operation operation)
    {
        Context context = new Context();

        context.object=object;
        context.type = type;
        context.operation=operation;
        bookSubscriptions.stream().forEach(cacheSubscription -> { cacheSubscription.add(context);});
    }


}
