package actorframework.example;

import actorframework.AbstractAktorBehavior;
import actorframework.Behavior;
import actorframework.Receive;

public class Client extends AbstractAktorBehavior<Object> {


    private Client() {
        super();


    }

   public static Behavior<Object> create() {

        return new Client();
    }




    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(Response.class,this::processResponse)
                //.onSignal(PostStop.class, signal->onPostStop() )
                .build();
    }


    private Behavior<Object> onPostStop() {
        System.out.println("Client actor stopped ");
        return this;
    }


    private Behavior<Object> processResponse(Response response) {

        System.out.println("Recd response " );

        return this;

    }


}
