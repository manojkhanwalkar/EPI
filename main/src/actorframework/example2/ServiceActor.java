package actorframework.example2;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;
import actors.data.Response;

import akka.actor.typed.PostStop;

import java.util.*;

public class ServiceActor extends AbstractAktorBehavior<Object> {


    private ServiceActor() {


    }

    public static Behavior<Object> create() {


        return new ServiceActor();
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessage(ServiceInfo.class, this::onServiceInfo)
                .onMessage(Heartbeat.class,this::onHeartBeat)
                .onMessage(Request.class,this::processRequest)
                .onSignal(PostStop.class, signal->onPostStop() )
                .build();
    }


    private Behavior<Object> onPostStop() {
        System.out.println("Service actor stopped " + getSelf());
        return this;
    }

    private Behavior<Object> processRequest(Request request) {

        System.out.println("Recd request");
        Response response = new Response(request.id, UUID.randomUUID().toString());
        request.replyTo.tell(response);
        return this;
    }

    private Set<AktorRef<Object>> services  = new HashSet<>();
    private Map<String,Long> heartbeatInfo = new HashMap<>();

    AktorRef<Object> heartBeatRef = null;

    private Behavior<Object> onServiceInfo(ServiceInfo info) {


        services.clear();
        info.services.stream().filter(s->!s.equals(getSelf())).forEach(s->services.add(s));

        if (heartBeatRef!=null)
        {
            heartBeatRef.tell(services);
        }
        else {
            heartBeatRef = spawn(HeartBeatActor.create(services), "heartbeat" + System.currentTimeMillis() );
            heartBeatRef.tell("start");
        }

        return this;
    }

    private Behavior<Object> onHeartBeat(Heartbeat heartbeat) {

       // System.out.println(getContext().getSelf() + " received heartbeat from " + heartbeat.sourceService);
        heartbeatInfo.put(heartbeat.sourceService.toString(),System.currentTimeMillis());
        return this;
    }



}
