package actors.data;

import akka.actor.typed.ActorRef;

public class Request implements Msg {

    public String id;

    public ActorRef<Object> replyTo;


    public Request(String id) {

        this.id = id;
    }

}
