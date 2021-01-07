package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscoveryActor extends AbstractBehavior<Registration> {

    List<ActorRef<Object>> serviceActors = new ArrayList<>();

    private ServiceDiscoveryActor(ActorContext<Registration> context) {
        super(context);

    }

    public static Behavior<Registration> create() {
        return Behaviors.setup(ServiceDiscoveryActor::new);
    }



    @Override
    public Receive<Registration> createReceive() {
        return newReceiveBuilder().onMessage(Registration.class,this::register)
                .build();
    }

    private Behavior<Registration> register(Registration register) {

        if (register.action.equals(Registration.Action.Register)) {
            System.out.println("Actor registered with discovery " + register.service);
            serviceActors.add(register.service);
        }
        else
        {
            System.out.println("Actor removed from discovery " + register.service);
            serviceActors.remove(register.service);

        }

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.services = serviceActors;
        serviceActors.forEach(s->{
            s.tell(serviceInfo);
        });

        return this;
    }


}
