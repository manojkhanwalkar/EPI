package ej.mr;

import java.util.List;

public interface Reducer<T,V> {

    void reduce(T t, List<V> values, collector<T,V> collector);



}
