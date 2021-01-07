package iot;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class IoTSupervisor extends AbstractBehavior<Void> {

    public static Behavior<Void> create() {
        return Behaviors.setup(IoTSupervisor::new);
    }

    private IoTSupervisor(ActorContext<Void> context) {
        super(context);
        context.getLog().info("IoT Application started");
    }

    // No need to handle any messages
    @Override
    public Receive<Void> createReceive() {
        return newReceiveBuilder().onSignal(PostStop.class, signal -> onPostStop()).build();
    }

    private IoTSupervisor onPostStop() {
        getContext().getLog().info("IoT Application stopped");
        return this;
    }
}
