package actors;

import actors.data.Heartbeat;
import actors.data.Request;
import actors.data.Response;
import actors.data.ServiceInfo;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.*;

public class ServiceActor extends AbstractBehavior<Object> {


    private ServiceActor(ActorContext<Object> context) {
        super(context);


    }

    public static Behavior<Object> create() {
        return Behaviors.setup(context-> new ServiceActor(context));
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
        System.out.println("Service actor stopped " + getContext().getSelf());
        return this;
    }

    private Behavior<Object> processRequest(Request request) {

        System.out.println("Recd request");
        Response response = new Response(request.id, UUID.randomUUID().toString());
        request.replyTo.tell(response);
        return this;
    }

    private Set<ActorRef<Object>> services  = new HashSet<>();
    private Map<String,Long> heartbeatInfo = new HashMap<>();

    ActorRef<Object> heartBeatRef = null;

    private Behavior<Object> onServiceInfo(ServiceInfo info) {


        services.clear();
        info.services.stream().filter(s->!s.equals(getContext().getSelf())).forEach(s->services.add(s));

        if (heartBeatRef!=null)
        {
            heartBeatRef.tell(services);
        }
        else {
            heartBeatRef = getContext().spawn(HeartBeatActor.create(services), "heartbeat" + System.currentTimeMillis() );
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
