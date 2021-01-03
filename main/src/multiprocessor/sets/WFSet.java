package multiprocessor.sets;

public interface WFSet<T> {

    boolean add(T t);

    boolean remove(T t);

    boolean contains(T t );

     void print();
}
