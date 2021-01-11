package actorframework.example2;

import actorframework.AktorRef;
import actors.data.Msg;

public class Request implements Msg {

    public String id;

    public AktorRef<Object> replyTo;


    public Request(String id) {

        this.id = id;
    }

}
