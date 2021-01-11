package actorframework.example2;


import actorframework.AbstractAktorBehavior;
import actorframework.AktorRef;
import actorframework.Behavior;
import actorframework.Receive;
import actors.data.Response;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ServiceClientActor extends AbstractAktorBehavior<Object> {

    AktorRef<Object> discoveryRef;

    private ServiceClientActor( AktorRef<Object> discoveryRef) {

        this.discoveryRef = discoveryRef;

    }


    public static Behavior<Object> create(AktorRef<Object> discoveryRef) {

            return new ServiceClientActor(discoveryRef);

    }



    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", this::appStart)
                .onMessage(ServiceInfo.class, this::serviceInfo)
                .build();
    }

    CompletableFuture<AktorRef<Object>>  serviceFuture;

    AktorRef<Object> service;

    private Behavior<Object> appStart() {

        System.out.println("Application client start ");
        ServiceInfoRequest serviceInfoRequest = new ServiceInfoRequest();
        serviceInfoRequest.requester = getSelf();

        serviceFuture =  new CompletableFuture<>();
        discoveryRef.tell(serviceInfoRequest);
         return this;

    }





        private Behavior<Object> serviceInfo(ServiceInfo info) {

        // send a request to each of the services and get back a response .
        // Print responses once we have it from all services
         CompletableFuture<Response> [] futures = new CompletableFuture[info.services.size()];

         for (int i=0;i<5;i++) {
             int index = 0;
             for (var service : info.services) {
                 CompletableFuture<Response> future = new CompletableFuture<>();
                 Request request = new Request(UUID.randomUUID().toString());

                 var child = spawn(RequestResponseActor.create(service, future), "RequestResponse" + System.nanoTime());
                 child.tell(request);
                 futures[index++] = future;
             }

             var result = CompletableFuture.allOf(futures).join();

             Arrays.stream(futures).forEach(f -> {
                 try {
                     System.out.println(f.get());
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 } catch (ExecutionException e) {
                     e.printStackTrace();
                 }
             });


             //TODO - introduce stop functionality
    /*         getContext().getChildren().forEach(c -> {
                 getContext().stop(c.narrow());
             });*/


         }
        return this;
    }




}
