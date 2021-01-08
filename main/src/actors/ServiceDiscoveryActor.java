package actors;

import actors.data.ServiceInfo;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceDiscoveryActor extends AbstractBehavior<Object> {

    List<ActorRef<Object>> serviceActors = new ArrayList<>();

    private ServiceDiscoveryActor(ActorContext<Object> context) {
        super(context);

    }

    public static final ServiceKey<Object> serviceKey =
            ServiceKey.create(Object.class, "serviceKey");

    public static Behavior<Object> create() {
        return Behaviors.setup(context->{

            context.getSystem()
                    .receptionist()
                    .tell(Receptionist.register(serviceKey, context.getSelf()));

            return new ServiceDiscoveryActor(context);
        });
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessage(Registration.class,this::register)
                //.onMessageEquals("service",this::service)
                .onMessage(ServiceInfoRequest.class, this::serviceInfoRequest)
                .build();
    }


    private Behavior<Object> serviceInfoRequest(ServiceInfoRequest serviceInfoRequest)
    {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.services = Collections.unmodifiableList(serviceActors);
        serviceInfoRequest.requester.tell(serviceInfo);
        return this;
    }

    private Behavior<Object> register(Registration register) {

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
