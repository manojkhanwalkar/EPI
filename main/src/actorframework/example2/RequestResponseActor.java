package actorframework.example2;

import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;

import actors.data.Response;
import akka.actor.typed.PostStop;

import java.util.concurrent.CompletableFuture;

public class RequestResponseActor extends AbstractAktorBehavior<Object> {


    private RequestResponseActor(AktorRef<Object> service , CompletableFuture<Response> future) {

        this.service = service;
        this.future  = future;
    }


    AktorRef<Object> service;
    CompletableFuture<Response> future ;
    public static Behavior<Object> create(AktorRef<Object> service , CompletableFuture<Response> future) {


            return new RequestResponseActor(service,future);

    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()

                .onMessage(Request.class, this::processRequest)
                .onMessage(Response.class, this::processResponse)
                .onSignal(PostStop.class, signal->onPostStop() )
                .build();
    }

    private Behavior<Object> onPostStop() {
        System.out.println("Request Response actor stopped " + getSelf());
        return this;
    }

    private Behavior<Object> processRequest(Request request) {
        request.replyTo=getSelf();
        service.tell(request);
        return this;
    }

    private Behavior<Object> processResponse(Response response) {

        future.complete(response);

        return this;
    }




}
