package java891011121314.patterns;

import java.util.*;

public class ObserverTester {

    @FunctionalInterface
    interface Observer<K,V>
    {
        void notify(String action, K key , V value);
    }

    @FunctionalInterface
    interface Subject<K,V>
    {
        void register(Observer<K,V> observer);
    }


    static class Cache <K,V> implements Subject<K,V>
    {
        Set<Observer> observers = new HashSet<>();

        public void register(Observer<K,V> observer)
        {
            observers.add(observer);
        }


        Map<K,V> map = new HashMap<>();

        public Cache()
        {

        }


        public void put(K key, V value)
        {
            map.put(key,value);

            observers.stream().forEach(o->o.notify("Put", key,value));
        }


        public void remove(K key)
        {
           V value =  map.remove(key);
            observers.stream().forEach(o->o.notify("Remove", key, value));

        }

    }


    public static void main(String[] args) {


        Cache<String,String> cache = new Cache();

        cache.register((a,k,v)->System.out.println(a + "  " + k + "  " + v));

        cache.put("H","W");
        cache.put("H1","W1");
        cache.put("H2","W2");
        cache.put("H3","W3");

        cache.remove("H3");



    }


}
