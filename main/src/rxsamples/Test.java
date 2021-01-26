package rxsamples;


import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;

public class Test {

    public static void main(String[] args) {

        Flowable<Integer> integerFlowable = Flowable.just(1, 2, 3, 4);

        Observable<Integer> integerObservable = Observable.just(1, 2, 3);
        integerFlowable = integerObservable
                .toFlowable(BackpressureStrategy.BUFFER);

        FlowableOnSubscribe<Integer> flowableOnSubscribe
                = flowable -> flowable.onNext(1);
        integerFlowable = Flowable
                .create(flowableOnSubscribe, BackpressureStrategy.BUFFER);

    }
}
