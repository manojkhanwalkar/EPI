package java891011121314;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class GenericFilterTest {

    @FunctionalInterface
    interface MyPredicate<T>
    {

        boolean accept(T t);

        default boolean another() { return true; }
    }

    @FunctionalInterface
    interface MySupplier<T>
    {
        T get();
    }

    @FunctionalInterface
    interface MyConsumer<T>
    {
        void apply(T t);
    }


    @FunctionalInterface
    interface MyFunction<T,R>
    {
        R convert(T t);

    }

    public static  <T> List<T> filter(List<T> list, MyPredicate<T> mf)
    {
        List<T> result = new ArrayList<>();
        list.stream().forEach(t->{if (mf.accept(t)) result.add(t); });

        return result;
    }


    public static <T> T create(MySupplier<T> supplier)
    {
        return supplier.get();
    }


    public static <T> void consume(MyConsumer<T> consumer, T t)
    {
        consumer.apply(t);
    }

    public static<T,R> R map(MyFunction<T,R> function, T t)
    {
        return function.convert(t);
    }


    public static void main(String[] args) {


        test1();
        test();

       // GenericFilterTest filterTest = new GenericFilterTest();

        List<Integer> numbers = List.of(create(()->1),3,5,10,25,30);

        List<Integer> numbers1 = List.of(7);


        var result = numbers.stream().sorted().flatMap(i-> numbers1.stream().map(j-> new int[]{i,j})).collect(toList());

        var result1 = numbers.stream().map(i->String.valueOf(i)).collect(toList());

        var result2 = numbers.stream().map(i-> new int[]{i,i}).map(i->i[0]).collect(toList());

        var result3 = numbers.stream().map(i-> {

            Random random = new Random();
            List<Integer> ints = new ArrayList<>();
            for (int j=0;j<i;j++)
            {
                ints.add(random.nextInt(j+1));
            }

            return ints.stream();

        }).collect(toList());

        var result4 = numbers.stream().flatMap(i-> {

            Random random = new Random();
            List<Integer> ints = new ArrayList<>();
            for (int j=0;j<i;j++)
            {
                ints.add(random.nextInt(j+5));
            }

            return ints.stream();

        }).collect(toList());



        numbers = filter(numbers,i->i%2==0);

        numbers.stream().forEach(System.out::println);


        List<String> strings = List.of(create(()->"Test1"), "Set1" , "Best1", "None");

        strings = filter(strings, s->s.contains("T") || s.contains("t"));

        strings.stream().forEach(s->{consume((s1)->System.out.println(s1),s);});

        Integer length = map(( s)->{ return s.length();},"Hello");

        System.out.println(length);

        String str = map((i)->String.valueOf(i), 100);

        System.out.println(str);

        Stream stream = strings.stream();


    }


    public static void test1()
    {
       /* var result = IntStream.range(2,100).boxed().collect(PrimeNumberCollector.toList());

        System.out.println("Primes result " + result);


        var result1 = IntStream.range(2,100).boxed().parallel().collect(OddEvenCollector.toCollect());

        System.out.println("Odd / Even  result " + result1);*/

      //  var result2 = IntStream.range(2,5).boxed().collect(new CountCollector());

       // System.out.println("count  result " + result2);


        var result1 = IntStream.range(0,10).boxed().collect(new FibonacciCollector());

        System.out.println("Fibonacci " + result1);

    }


    public static void test()
    {
        List<Integer> evens = Arrays.asList(2, 4, 6);
        List<Integer> odds = Arrays.asList(3, 5, 7);
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11);

        List numbers = Stream.of(evens, odds, primes)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        System.out.println("flattend list: " + numbers);



        List<List<Integer>> listOfListOfNumber = new ArrayList<>();
        listOfListOfNumber.add(Arrays.asList(2, 4));
        listOfListOfNumber.add(Arrays.asList(3, 9));
        listOfListOfNumber.add(Arrays.asList(4, 16));


      /*  var listOfIntegers = listOfListOfNumber.stream()
                .flatMap( list -> list.stream())
                .collect(Collectors.toList());*/


   var listOfIntegers = listOfListOfNumber.stream()
                .flatMap( list -> list.stream())
                .collect(MyCollector.toList());


        System.out.println("List of Integers " + listOfIntegers);

    }

}
