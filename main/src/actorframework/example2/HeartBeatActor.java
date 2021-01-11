package actorframework.example2;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;
import akka.actor.typed.PostStop;

import java.util.Set;

public class HeartBeatActor extends AbstractAktorBehavior<Object> {


    private HeartBeatActor( Set<AktorRef<Object>> services) {

        this.services = services;

    }

    public static Behavior<Object> create(final Set<AktorRef<Object>> services) {
        return new HeartBeatActor(services);
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", this::startHeartBeat)
                .onMessage(Set.class, this::updateServices)
                .onSignal(PostStop.class, signal->onPostStop() )
                .build();
    }

    private Set<AktorRef<Object>> services  ;


    private Behavior<Object> onPostStop() {
        System.out.println("Heartbeat stopped " );
        return this;
    }

    private Behavior<Object> updateServices(Set<AktorRef<Object>> services)
    {

        this.services =  services;

        return this;
    }

    private Behavior<Object> startHeartBeat() {

         //   System.out.println("Started heartbeat " + getContext().getSelf());

           Heartbeat heartbeat = new Heartbeat();
           heartbeat.sourceService = getSelf();
           services.forEach(s-> s.tell(heartbeat));

           try {
               Thread.sleep(10000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           getSelf().tell("start");

        return this;
    }




}
