package actors.data;

import akka.actor.typed.ActorRef;

public class Heartbeat {
    public ActorRef<Object> sourceService;
}
