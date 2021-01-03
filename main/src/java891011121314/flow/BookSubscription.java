package java891011121314.flow;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Flow;

public class BookSubscription implements Flow.Subscription {

    Flow.Subscriber<? super Context> subscriber ;

    Flow.Publisher<Context> publisher;



     Queue<Context> pending = new ArrayDeque<>();

     public void add(Context context)
     {
         pendingCount++;
         pending.add(context);
         process();
     }

    public BookSubscription(Flow.Subscriber<? super Context> subscriber, Flow.Publisher<Context> publisher) {

        this.subscriber = subscriber;

        this.publisher = publisher;
    }

    long pendingCount=0;

     private void process()
     {

         while(!pending.isEmpty() && pendingCount--!=0)
         {
             subscriber.onNext(pending.remove());
         }
     }

    @Override
    public void request(long l) {

        pendingCount+=l;

        process();
    }

    @Override
    public void cancel() {



    }
}
