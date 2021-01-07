package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceFactoryActor extends AbstractBehavior<Object> {

    List<ActorRef<Object>> serviceActors = new ArrayList<>();

    ActorRef<Registration> serviceDiscovery;

    private ServiceFactoryActor(ActorContext<Object> context, ActorRef<Registration> serviceDiscovery) {
        super(context);

        this.serviceDiscovery = serviceDiscovery;



    }


    public static Behavior<Object> create(ActorRef<Registration> serviceDiscovery) {
        return  Behaviors.setup(context-> new ServiceFactoryActor(context,serviceDiscovery));
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessageEquals("start",this::onStart)
                .build();
    }

    volatile boolean start = true;

    private Behavior<Object> onStart() {

        for (int i=0;i<5;i++) {

            start = true;
            while (start) {


                var serviceActor = getContext().spawn(ServiceActor.create(), UUID.randomUUID().toString());


                serviceActors.add(serviceActor);


                Registration register = new Registration();
                register.service = serviceActor;
                register.action = Registration.Action.Register;

                serviceDiscovery.tell(register);

                if (serviceActors.size() == 10)
                    start = false;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            while (serviceActors.size() > 0) {
                var serviceActor = serviceActors.remove(0);

                Registration register = new Registration();
                register.service = serviceActor;
                register.action = Registration.Action.Cancel;

                serviceDiscovery.tell(register);

                getContext().stop(serviceActor);


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }


        return Behaviors.stopped();
    }





}
