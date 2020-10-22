package offheapmap;


import java.nio.ByteBuffer;
import java.util.Optional;

//TODO - make generic

//TODO - associate with a file

/*
MappedByteBuffer out = file.getChannel()
                                        .map(FileChannel.MapMode.READ_WRITE, 0, length);
 */
public class MemMap<K,V> {

    int recordSize;
    int totalElements;

    int memMapSize ;

    ByteBuffer buffer ;

    static final byte EMPTY = (byte)1;
    static final byte OCCUPIED = (byte)2;
    static final byte DELETED = (byte)3;


    Serializer keySerializer ;
    Serializer valueSerializer;

    public MemMap(int recordSize, int totalElements, Serializer keySerializer , Serializer valueSerializer)
    {
        this.recordSize = recordSize;
        this.totalElements = totalElements;

        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;

        memMapSize = recordSize*totalElements;

        buffer = ByteBuffer.allocateDirect(memMapSize);

        System.out.println(buffer.mark());

        for (int i=0;i<memMapSize;i++)
        {
            buffer.put(EMPTY);
        }



    }


    // assumes record size is not exceeded

    // also assumes max elements not exceeded

    public void put(K key, V value)
    {

        int hash = Math.abs(key.hashCode());

        int index = hash%totalElements;

        int location = index*recordSize;

        buffer.position(location);

    //TODO - have to check if the key is same , in which case it will be overwritten.
        while(buffer.get()!=EMPTY)
        {
            ++index;
            location = index*recordSize;

            if (location>=memMapSize)
            {
                location=0;
                index = 0;
            }
            buffer.position(location);

        }

            buffer.position(location);
            byte[] b = keySerializer.serialize(key) ;
            buffer.put(OCCUPIED);
            buffer.putInt(b.length);
            buffer.put(b);
            b = valueSerializer.serialize(value);
            buffer.putInt(b.length);
            buffer.put(b);

    }


    public Optional<V> get(K key)
    {
        int hash = Math.abs(key.hashCode());

        int index = hash%totalElements;

        int location = index*recordSize;

        buffer.position(location);


        byte type;
        while((type=buffer.get())!=EMPTY)
        {

            // read the record


            int len = buffer.getInt();

            byte[] b = new byte[len];

            buffer.get(b);

            K key1 = (K)keySerializer.deserialize(b);

            if (type==DELETED && key.equals(key1))
            {
                break;
            }

            if (key.equals(key1))// found
            {
                len = buffer.getInt();
                b = new byte[len];
                buffer.get(b);
                V value =  (V)valueSerializer.deserialize(b);

                return Optional.of(value);
            }

            ++index;
            location = index*recordSize;

            if (location>=memMapSize)
            {
                location=0;
                index = 0;
            }
            buffer.position(location);

        }

        return Optional.empty();


    }


    public void delete(K key)
    {
        int hash = Math.abs(key.hashCode());

        int index = hash%totalElements;

        int location = index*recordSize;

        buffer.position(location);


        byte type;
        while((type=buffer.get())!=EMPTY)
        {

            // read the record


            int len = buffer.getInt();

            byte[] b = new byte[len];

            buffer.get(b);

            K key1 = (K)keySerializer.deserialize(b);

            if (type==DELETED && key.equals(key1))
            {
                break;  // already deleted , return
            }

            if (key.equals(key1))// found
            {
               buffer.position(location);
               buffer.put(DELETED);

                return;
            }

            ++index;
            location = index*recordSize;

            if (location>=memMapSize)
            {
                location=0;
                index = 0;
            }
            buffer.position(location);

        }

        return ;


    }



    //TODO - add option to delete


    protected static<K,V> MemMap<K, V> resize(MemMap<K,V> orig)
    {

        MemMap<K,V> resized = new MemMap<>(orig.recordSize,orig.totalElements*2, orig.keySerializer,orig.valueSerializer);


        ByteBuffer buffer = orig.buffer;

        for (int i=0;i<orig.totalElements;i++)
        {
            if (buffer.get()!=EMPTY) {
                int len = buffer.getInt();
                byte[] b = new byte[len];
                buffer.get(b);
                K key = (K) orig.keySerializer.deserialize(b);
                len = buffer.getInt();
                b = new byte[len];
                buffer.get(b);
                V value = (V) orig.valueSerializer.deserialize(b);

                resized.put(key, value);
            }
            buffer.position(orig.recordSize*i);

        }



        return resized;




    }


 /*   public static void main(String[] args) {

        StringSerializer ser = new StringSerializer();

        MemMap map = new MemMap(100, 10000,ser,ser );

        map.put("Hello" , "Generic World");

        map.put("HELLO" , "GENERIC WORLD");

        System.out.println(map.get("Hello"));

        System.out.println(map.get("HELLO"));


    }*/




}
