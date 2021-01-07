package actors;

import akka.actor.typed.ActorRef;

public class Heartbeat {
    ActorRef<Object> sourceService;
}
