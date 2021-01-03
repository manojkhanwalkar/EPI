package ej.mr;

public interface Mapper<T,V> {

    void map(T t, collector<T,V> collector);



}
