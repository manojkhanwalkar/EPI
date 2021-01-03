package ej.mr;

public interface collector<T,V> {

    void collect(pair p);

    void complete();

    String getFileName();

}
