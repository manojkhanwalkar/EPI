package iot;


import akka.actor.typed.ActorSystem;

public class IoTMain {

    public static void main(String[] args) {
        // Create ActorSystem and top level supervisor
        ActorSystem.create(IoTSupervisor.create(), "iot-system");
    }
}