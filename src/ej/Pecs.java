package ej;

import java.util.ArrayList;
import java.util.List;

public class Pecs {

    static abstract class Message
    {

        int id;
    }

    static abstract class FixMessage extends Message
    {

    }

    static class Order extends FixMessage
    {

    }

    static abstract class JMSMessage extends Message{

    }

    static class TextMessage extends JMSMessage
    {

    }


    List<? super Message> list1 = new ArrayList<>();
    List<? extends Message> list2 = new ArrayList<>();

    public void test()
    {

        list1.add(new Order());
        list1.add(new TextMessage());

        System.out.println(list1);

        list2 = copy(list1);

        Message m1 = list2.get(0);

        Message m2 = list2.get(1);

        System.out.println(m1) ;
        System.out.println(m2);


    }

     private List<? extends Message> copy(List list1)
     {
        return list1;

     }

    public static void main(String[] args) {

        Pecs p = new Pecs();

        p.test();

    }



}
