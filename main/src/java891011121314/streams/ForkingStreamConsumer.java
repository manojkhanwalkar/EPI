package java891011121314.streams;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ForkingStreamConsumer<T> implements Consumer<T>, Results {

    static final Object END_OF_STREAM = new Object();

    final List<BlockingQueue<T>> queues;

    final Map<Object, Future<?>> actions;

    public ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
        this.queues = queues;
        this.actions = actions;
    }

    @Override
    public void accept(T t) {

        queues.forEach(q->q.add(t));

    }

    public void finish()
    {
        accept((T) END_OF_STREAM);
    }

    @Override
    public <R> R get(String key) {
        try {
            return ((Future<R>)actions.get(key)).get();
        } catch (Exception e) {

            throw new RuntimeException();

        }


    }


}
