package loadbalancer;

public interface Counter {

    default void increment(Service service) {}

    default void decrement(Service service) {}
}
