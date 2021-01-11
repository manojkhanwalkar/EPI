package actorframework.example2;


import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceDiscoveryActor extends AbstractAktorBehavior<Object> {

    List<AktorRef<Object>> serviceActors = new ArrayList<>();

    private ServiceDiscoveryActor() {
        super();

    }



    public static Behavior<Object> create() {


            return new ServiceDiscoveryActor();

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
