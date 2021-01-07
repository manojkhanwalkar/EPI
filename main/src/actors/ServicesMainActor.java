package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ServicesMainActor extends AbstractBehavior<Object> {

    ActorRef<Object> factoryRef;
    ActorRef<Registration> discoveryRef;

    private ServicesMainActor(ActorContext<Object> context) {
        super(context);
        var discovery = ServiceDiscoveryActor.create();
         discoveryRef = context.spawn(discovery,"discovery");
         factoryRef = context.spawn(ServiceFactoryActor.create(discoveryRef),"factory" );

    }


    public static Behavior<Object> create() {
        return  Behaviors.setup(context-> new ServicesMainActor(context));
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start" , this::onStart)
                .build();
    }

    private Behavior<Object> onStart() {

        factoryRef.tell("start");
        return this;
    }


}
