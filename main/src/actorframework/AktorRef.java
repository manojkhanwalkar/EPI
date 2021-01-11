package actorframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AktorRef<T> {

    Behavior<T> behavior ;
    final String name;

    ExecutorService service ;
    public <M> void tell(M object)
    {

        switch(AktorSystem.strategy)
        {
            case Sync:
                process(object);
            case Dedicated:
            case ThreadPool:
                service.submit(()->{process(object);});

        }



    }

    private <M> void process(M object)
    {
        Class c = object.getClass();

        if (object instanceof String)
        {
            var supplier = behavior.getReceive().getStringHandler((String)object);
            if (supplier!=null) {
                supplier.get();
                return;
            }

        }

        var handler = behavior.getReceive().getHandler(c);
        handler.apply(object);
    }

    public  AktorRef(Behavior<T> behavior, String name)
    {
        this.behavior = behavior;
        this.name = name;
        behavior.setAktorRef(this);

        if (AktorSystem.strategy== AktorSystem.Strategy.Dedicated)
            service = Executors.newFixedThreadPool(1);
        else if (AktorSystem.strategy == AktorSystem.Strategy.ThreadPool)  // todo - fix message sequencing issue for same aktor
            service = AktorSystem.service;


    }
}
