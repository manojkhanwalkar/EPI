package ej;

import java.lang.ref.Cleaner;

public class ObjectCreation {

    public static void main(String[] args) throws Exception  {

        for (int i=0;i<100;i++) {
            System.out.println(sum());

            Thread.sleep(10000);

        }

    }


     class MyLong
    {

        final long l ;
        public MyLong(long l)
        {
            this.l = l;

        }

        public MyLong add(long n)
        {
            return new MyLong(l+n);
        }

        public long valueOf()
        {
            return l;
        }
    }

    private static class State implements Runnable {

        long l;
        public State(long l)
        {
            this.l = l;
        }
        public void run() {
            System.out.println("Cleaning action " + l + " " + Thread.currentThread().getName());
        }
    }

    public MyLong nonStaticTest()
    {

        MyLong sum = new MyLong(0L);

        return sum;
    }

    static long sum() {

        class MyInt {

            int num;

            public MyInt(int num)
            {
                this.num = num;
            }

            public int get()
            {
                return num;
            }

        }
        Cleaner cleaner = Cleaner.create();


        ObjectCreation objectCreation = new ObjectCreation();

        MyLong sum = objectCreation.nonStaticTest();



        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            cleaner.register(sum, new State(sum.valueOf()));


            sum = sum.add(new MyInt(i).get());
        }

        return sum.valueOf();


    }

}