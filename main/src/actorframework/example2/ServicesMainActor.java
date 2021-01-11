package actorframework.example2;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;

public class ServicesMainActor extends AbstractAktorBehavior<Object> {

    AktorRef<Object> factoryRef;
    AktorRef<Object> discoveryRef;

    AktorRef<Object> clientRef;

    private ServicesMainActor() {


    }


    public static Behavior<Object> create() {

        return new ServicesMainActor();
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start" , this::onStart)
                .build();
    }

    private Behavior<Object> onStart() {


        var discovery = ServiceDiscoveryActor.create();
        discoveryRef = spawn(discovery,"discovery");
        factoryRef = spawn(ServiceFactoryActor.create(discoveryRef),"factory" );
        clientRef = spawn(ServiceClientActor.create(discoveryRef),"client" );

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
