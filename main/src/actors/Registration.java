package actors;

import akka.actor.typed.ActorRef;

public class Registration {

    public enum Action { Register , Cancel}
    ActorRef<Object> service;
    Action action;
}
