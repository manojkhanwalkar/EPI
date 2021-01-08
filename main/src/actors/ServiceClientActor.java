package actors;

import actors.data.Request;
import actors.data.Response;
import actors.data.ServiceInfo;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ServiceClientActor extends AbstractBehavior<Object> {

    ActorRef<Object> discoveryRef;

    private ServiceClientActor(ActorContext<Object> context, ActorRef<Object> discoveryRef) {
        super(context);
        this.discoveryRef = discoveryRef;

    }


    public static Behavior<Object> create(ActorRef<Object> discoveryRef) {
        return Behaviors.setup(context->{

            return new ServiceClientActor(context,discoveryRef);
        });
    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", this::appStart)
                .onMessage(ServiceInfo.class, this::serviceInfo)
                .onMessage(Response.class, this::processResponse)
                .build();
    }

    CompletableFuture<ActorRef<Object>>  serviceFuture;

    ActorRef<Object> service;

    private Behavior<Object> appStart() {

        System.out.println("Application client start ");
        ServiceInfoRequest serviceInfoRequest = new ServiceInfoRequest();
        serviceInfoRequest.requester = getContext().getSelf();

        serviceFuture =  new CompletableFuture<>();
        discoveryRef.tell(serviceInfoRequest);
         return this;

    }

    private Behavior<Object> processResponse(Response response) {
      System.out.println("Recd response");
        var future = responses.get(response.requestId);

        future.complete(response);

        return this;
    }


        Map<String,CompletableFuture<Response>> responses = new HashMap<>();

        private Behavior<Object> serviceInfo(ServiceInfo info) {

        serviceFuture.complete(info.services.get(0));

        System.out.println("Service info received from discovery");

        try {
            service = serviceFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("=================> Received service handle ");

        // send a request to each of the services and get back a response .
        // Print responses once we have it from all services
         CompletableFuture<Response> [] futures = new CompletableFuture[info.services.size()];
         int index=0;
        for ( var service : info.services)
        {
            CompletableFuture<Response> future = new CompletableFuture<>();
            Request request = new Request(UUID.randomUUID().toString());
            request.replyTo=getContext().getSelf();
            responses.put(request.id,future);
            futures[index++] = future;
            service.tell(request);
        }

     /*   var result = CompletableFuture.allOf(futures).join();

            Arrays.stream(futures).forEach(f->{
                try {
                    System.out.println(f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });*/
        return this;
    }




}
