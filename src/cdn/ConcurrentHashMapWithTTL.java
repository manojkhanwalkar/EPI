package cdn;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapWithTTL<K,V> {


    static int TTL = 10 ; // 10 secs

    static class ValueTTLTuple<V>
    {
        V value;
        long insertTime;


        public ValueTTLTuple(V value, long insertTime)
        {
            this.value = value;
            this.insertTime = insertTime;
        }
    }

    ConcurrentMap<K,ValueTTLTuple<V>> map = new ConcurrentHashMap<>();

    public void put(K key , V value)
    {
       // map.put(key,new ValueTTLTuple<>(value,System.currentTimeMillis()));

        map.computeIfAbsent(key , k-> {

            return new ValueTTLTuple<>(value,System.currentTimeMillis());
        });
    }


    public Optional<V> get(K key)
    {

        ValueTTLTuple<V> tuple = map.get(key);

        if (tuple==null)
        {
            return Optional.empty();
        }

        long current = System.currentTimeMillis();

       // System.out.println(current + "  " + tuple.insertTime + "  " + (current-tuple.insertTime));

        if (current-tuple.insertTime > TTL*1000)
        {
            map.remove(key);
            return Optional.empty();
        }
        else
        {
            return Optional.of(tuple.value);
        }

    }
}
