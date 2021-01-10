package actorframework.example;

import actorframework.AbstractAktorBehavior;
import actorframework.Behavior;
import actorframework.Receive;
import akka.actor.typed.PostStop;

import java.util.UUID;

public class Server extends AbstractAktorBehavior<Object> {


    private Server() {



    }


    public static Behavior<Object> create() {
        return new Server();

    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(Request.class,this::processRequest)
                .onSignal(PostStop.class, signal->onPostStop() )
                .build();
    }


    private Behavior<Object> onPostStop() {
        System.out.println("Client actor stopped " );
        return this;
    }

    private Behavior<Object> processRequest(Request request) {

        System.out.println("Recd request");
        Response response = new Response(request.id, UUID.randomUUID().toString());
        request.replyTo.tell(response);
        return this;
    }



}
