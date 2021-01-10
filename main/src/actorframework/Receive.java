package actorframework;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Receive<T> {

    Map<Class<?> , Function<?, Behavior<T> >> handlers = new HashMap<>();

    public <M> void addHandler(final Class<M> type, Function<M, Behavior<T>> handler )
    {
        handlers.put(type,handler);
    }

    //TODO - add a default handler feature , if no handler is found;
    public <M> Function<M, Behavior<T>> getHandler(Class c)
    {
        return (Function<M, Behavior<T>>) handlers.get(c);
    }

    Map<String,Supplier<Behavior<T>>> stringHandlers = new HashMap<>();

    public void addStringHandler(String msg, Supplier<Behavior<T>> handler) {

        stringHandlers.put(msg,handler);
    }

    public <M> Supplier<Behavior<T>>  getStringHandler(String msg)
    {
        return  stringHandlers.get(msg);
    }
}
