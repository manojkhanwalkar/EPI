package actorframework;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAktorBehavior<T>  implements Behavior<T> {

    List<AktorRef<T>> children = new ArrayList<>();

    Receive<T> receive;

    public Receive<T> getReceive()
    {
        return receive;
    }

    public AbstractAktorBehavior() {

       receive =  createReceive();
    }

    protected abstract Receive<T> createReceive();

    protected ReceiveBuilder<T> newReceiveBuilder() {

        return new ReceiveBuilder<>();
    }

    protected AktorRef<T> spawn(Behavior<T> behavior, String client)
    {
        var actorRef =  new AktorRef<>(behavior,client);
        children.add(actorRef);

        return actorRef;

    }
}
