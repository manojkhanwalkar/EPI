package java891011121314.flow;

import java.util.concurrent.Flow;

public class ConsoleWriter implements Flow.Subscriber<Context> {

    Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

        this.subscription = subscription;

        subscription.request(1);

    }

    @Override
    public void onNext(Context context) {

        System.out.println(context);

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
}
