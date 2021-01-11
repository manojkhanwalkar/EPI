package actorframework;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AktorSystem {

    static String name = "/user";

    static ConcurrentMap<String,AbstractAktorBehavior>  actors = new ConcurrentHashMap<>();

    static boolean initialized = false;

    public enum Strategy { Sync , Dedicated , ThreadPool };

     static Strategy strategy;

     static ExecutorService service ;

    public static <T> AktorRef<T>  create(Behavior<T> behavior , String behaviourName, Strategy s )
    {
        if (initialized)
            throw new RuntimeException("Actor system already initialized");
        strategy = s;

        if (strategy==Strategy.ThreadPool)
        {
            service = Executors.newCachedThreadPool();
        }
        // create and return an actor ref
        var actorRef =  new AktorRef<>(behavior,behaviourName);



        initialized=true;
        return actorRef;
    }

    static <T> void register(AbstractAktorBehavior<T> behavior , String behaviourName)
    {
        // add to map .
    }

    public static void destroy()
    {
        initialized=true;
        // stop all the actors .
        actors.clear();
    }


}
