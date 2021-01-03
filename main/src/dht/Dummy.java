package dht;

import java.util.UUID;

public class Dummy {

    String key;

    int location;



    public Dummy()
    {
            key = UUID.randomUUID().toString();


    }


    @Override
    public String toString() {
        return "Dummy{" +
                "location=" + location +
                '}';
    }
}
