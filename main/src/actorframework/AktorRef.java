package actorframework;

public class AktorRef<T> {

    Behavior<T> behavior ;
    final String name;
    public <M> void tell(M object)
    {
//TODO - add Async behaviour later

        Class c = object.getClass();

        if (object instanceof String)
        {
            var supplier = behavior.getReceive().getStringHandler((String)object);
            if (supplier!=null) {
                supplier.get();
                return;
            }

        }

        var handler = behavior.getReceive().getHandler(c);
        handler.apply(object);

    }

    public  AktorRef(Behavior<T> behavior, String name)
    {
        this.behavior = behavior;
        this.name = name;

    }
}
