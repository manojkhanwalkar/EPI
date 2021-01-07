package actors;

import akka.actor.typed.ActorRef;

import java.util.List;

public class ServiceInfo {

    List<ActorRef<Object>> services;

}
