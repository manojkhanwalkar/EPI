package actors;

import akka.actor.typed.ActorSystem;


public class AkkaQuickstart {
  public static void main(String[] args) throws Exception {

    ActorSystem<Object> system = ActorSystem.create(ServicesMainActor.create(),"main");
    system.tell("start");

  }
}
