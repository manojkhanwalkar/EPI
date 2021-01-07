package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Set;

public class HeartBeatActor extends AbstractBehavior<Object> {


    private HeartBeatActor(ActorContext<Object> context, Set<ActorRef<Object>> services) {
        super(context);
        this.services = services;

    }

    public static Behavior<Object> create(final Set<ActorRef<Object>> services) {
        return Behaviors.setup(context-> new HeartBeatActor(context,services));
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", this::startHeartBeat)
                .onMessage(Set.class, this::updateServices)
                .onSignal(PostStop.class,signal->onPostStop() )
                .build();
    }

    private Set<ActorRef<Object>> services  ;


    private Behavior<Object> onPostStop() {
        System.out.println("Heartbeat stopped " + getContext().getSelf());
        return this;
    }

    private Behavior<Object> updateServices(Set<ActorRef<Object>> services)
    {

        this.services =  services;

        return this;
    }

    private Behavior<Object> startHeartBeat() {

         //   System.out.println("Started heartbeat " + getContext().getSelf());

           Heartbeat heartbeat = new Heartbeat();
           heartbeat.sourceService = getContext().getSelf();
           services.forEach(s-> s.tell(heartbeat));

           try {
               Thread.sleep(10000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           getContext().getSelf().tell("start");

        return this;
    }




}
