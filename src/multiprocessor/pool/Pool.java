package multiprocessor.pool;

public interface Pool<T> {

    T get();
    void put(T t );
}
