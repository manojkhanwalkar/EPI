package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ServicesMainActor extends AbstractBehavior<Object> {

    ActorRef<Object> factoryRef;
    ActorRef<Object> discoveryRef;

    ActorRef<Object> clientRef;

    private ServicesMainActor(ActorContext<Object> context) {
        super(context);

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


        var discovery = ServiceDiscoveryActor.create();
        discoveryRef = getContext().spawn(discovery,"discovery");
        factoryRef = getContext().spawn(ServiceFactoryActor.create(discoveryRef),"factory" );
        clientRef = getContext().spawn(ServiceClientActor.create(discoveryRef),"client" );

        factoryRef.tell("start");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientRef.tell("start");
        return this;
    }


}
