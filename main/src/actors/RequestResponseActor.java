package actors;

import actors.data.Request;
import actors.data.Response;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.concurrent.CompletableFuture;

public class RequestResponseActor extends AbstractBehavior<Object> {


    private RequestResponseActor(ActorContext<Object> context,ActorRef<Object> service , CompletableFuture<Response> future) {
        super(context);

        this.service = service;
        this.future  = future;
    }


    ActorRef<Object> service;
    CompletableFuture<Response> future ;
    public static Behavior<Object> create(ActorRef<Object> service , CompletableFuture<Response> future) {
        return Behaviors.setup(context->{

            return new RequestResponseActor(context,service,future);
        });
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
        System.out.println("Request Response actor stopped " + getContext().getSelf());
        return this;
    }

    private Behavior<Object> processRequest(Request request) {
        request.replyTo=getContext().getSelf();
        service.tell(request);
        return this;
    }

    private Behavior<Object> processResponse(Response response) {

        future.complete(response);

        return this;
    }




}
