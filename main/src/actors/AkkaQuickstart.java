package actors;

import akka.actor.typed.ActorSystem;

public class AkkaQuickstart {
  public static void main(String[] args) {

    ActorSystem<Object> system = ActorSystem.create(ServicesMainActor.create(),"main");
    system.tell("start");


  }
}
