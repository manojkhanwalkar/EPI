package actorframework;

public interface Behavior<T>{

     Receive<T> getReceive();
}
