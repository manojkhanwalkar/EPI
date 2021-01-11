package actorframework.example;


import actorframework.AktorRef;
import actorframework.AktorSystem;

public class AkkaQuickstart {
  public static void main(String[] args) throws Exception {

    AktorRef<Object> system = AktorSystem.create(MainActor.create(),"main", AktorSystem.Strategy.Dedicated);
    system.tell("start");

  }
}
