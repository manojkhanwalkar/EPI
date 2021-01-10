package actorframework;

import akka.actor.typed.Signal;

import java.util.function.Function;
import java.util.function.Supplier;

public class ReceiveBuilder<T> {

    Receive<T> receive = new Receive<>();

    public Receive<T> build()
    {
        return receive;
    }


    public ReceiveBuilder<T> onMessageEquals(String msg, Supplier<Behavior<T>> handler) {

        receive.addStringHandler(msg,handler);
        return this;
    }


    public <M extends Signal> ReceiveBuilder<T> onSignal(final Class<M> type, final Function<M, Behavior<T>> handler) {

        return this;
    }

    public <M> ReceiveBuilder<T> onMessage(final Class<M> type, Function<M, Behavior<T>> handler ) {

        receive.addHandler(type,handler);
        return this;
    }
}
