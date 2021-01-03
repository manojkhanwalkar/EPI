package java891011121314;

public class PrettyPrint<T> {

    interface Fruit
    {
         String getType();
    }


    static class Apple implements Fruit{

        enum Type { Green , Red, Delicious};

        Type type = Type.Green; // default

        public String getType()
        {
            return type.toString();
        }

        int weight;

    }


    static class Orange implements Fruit {

        enum Type { Mandarin , Juice, Tangy};

        Type type = Type.Juice; // default

        int weight;

        public String getType()
        {
            return type.toString();
        }


    }

    interface Formatter<T>
    {
        String accept(T t);
    }

    public void prettyPrint(T t,Formatter<T> formatter )
    {
            System.out.println(formatter.accept(t));
    }

    public static void main(String[] args) {

        PrettyPrint<Fruit> pp = new PrettyPrint<>();

        pp.prettyPrint(new Apple(),(a)-> a.getType());

        PrettyPrint<Orange> pp1 = new PrettyPrint<>();



        pp1.prettyPrint(new Orange(),(a)-> a.toString());



    }

}
