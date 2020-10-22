package offheapmap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class EfficientHashMap<K,V> {

    MemMap<K,V>  memMap ;

    Map<K,V> map = new CustomLinkedHashMap<>(10);


    public EfficientHashMap(int recordSize, int totalElements, Serializer keySerializer , Serializer valueSerializer)
    {

        memMap = new MemMap<>(recordSize,totalElements,keySerializer,valueSerializer);


    }

    public void put(K key, V value) {

        memMap.put(key,value);
    }

    public V get(K key)
    {
        V value = map.get(key);
        if (value!=null)
            return  value;

        Optional<V> perValue =  memMap.get(key);
        if (perValue.isPresent())
        {
            map.put(key,perValue.get());
            return perValue.get();
        }
        else
        {
            return null;
        }

    }

    public void delete(K key)
    {
        map.remove(key);

        memMap.delete(key);


    }

    protected void resize()
    {
        memMap = MemMap.resize(memMap);
    }



        static class CustomLinkedHashMap<K,V> extends LinkedHashMap<K,V>
    {

        int size;

        public CustomLinkedHashMap(int size)
        {
            super(size);

            this.size = size;
        }


        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {

            if (this.size()>=size) {
               // System.out.println("Removing from memory" + eldest);
                return true;
            }
            else
            {
                return false;
            }

        }
    }


    public static void main(String[] args) {

        StringSerializer ser = new StringSerializer();
        IntegerSerializer iser = new IntegerSerializer();

        EfficientHashMap<Integer,String> map = new EfficientHashMap<>(100, 10000,iser,ser );


        for (int i=0;i<250;i++) {
            map.put(i, "Generic World int key " + i);

           // map.put(200, "GENERIC WORLD INT key");

        }

        for (int i=0;i<250;i++) {

            System.out.println(map.get(i));


        }

        System.out.println();



        map.resize();

        System.out.println(map.get(100));

        System.out.println(map.get(200));


        map.delete(100);

        System.out.println("Retrieving deleted key " + map.get(100));


        map.put(100,"Key put again after deletion");

        System.out.println(map.get(100));



    }



}
