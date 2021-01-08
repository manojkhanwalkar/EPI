package actors.data;

import akka.actor.typed.ActorRef;

import java.util.List;

public class ServiceInfo {

    public List<ActorRef<Object>> services;

}
