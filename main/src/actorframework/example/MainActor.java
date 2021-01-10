package actorframework.example;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;

import java.util.UUID;

public class MainActor extends AbstractAktorBehavior<Object> {

    AktorRef<Object> clientRef;
    AktorRef<Object> serverRef;


    private MainActor() {
        //super(context);

    }


    public static Behavior<Object> create() {
        return new MainActor();
    }


    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start" , this::onStart)
                .build();
    }



    private Behavior<Object> onStart() {


        clientRef = spawn(Client.create(),"client" );
        serverRef = spawn(Server.create(), "server");

        Request request= new Request(UUID.randomUUID().toString());
        request.replyTo=clientRef;
        serverRef.tell(request);

        return this;
    }


}
