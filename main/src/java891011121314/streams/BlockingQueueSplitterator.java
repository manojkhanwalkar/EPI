package java891011121314.streams;

import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class BlockingQueueSplitterator<T> implements Spliterator<T> {


    private final BlockingQueue<T> queue;


    public BlockingQueueSplitterator(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {

        T t = null;
        try {
            t = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (t!=ForkingStreamConsumer.END_OF_STREAM)
        {
            consumer.accept(t);
            return true;
        }
        else
        {
            return false;
        }

    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
