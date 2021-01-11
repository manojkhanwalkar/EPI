package actorframework.example2;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceFactoryActor extends AbstractAktorBehavior<Object> {

    List<AktorRef<Object>> serviceActors = new ArrayList<>();

    AktorRef<Object> serviceDiscovery;

    private ServiceFactoryActor(AktorRef<Object> serviceDiscovery) {

        this.serviceDiscovery = serviceDiscovery;



    }


    public static Behavior<Object> create(AktorRef<Object> serviceDiscovery) {
        return  new ServiceFactoryActor(serviceDiscovery);
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessageEquals("start",this::onStart)
                .build();
    }

    volatile boolean start = true;

    private Behavior<Object> onStart() {

        for (int i=0;i<5;i++) {

                var serviceActor = spawn(ServiceActor.create(), UUID.randomUUID().toString());

                serviceActors.add(serviceActor);


                Registration register = new Registration();
                register.service = serviceActor;
                register.action = Registration.Action.Register;

                serviceDiscovery.tell(register);



            }



        return this;
    }





}
