package actorframework;

public interface Behavior<T>{

     Receive<T> getReceive();

     void setAktorRef(AktorRef<T> self);
}
