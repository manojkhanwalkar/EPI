package java891011121314;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class FibonacciCollector implements Collector<Integer,List<Integer>,List<Integer>> {




/*    public static boolean  isPrime(List<Integer> primes, int candidate)
    {

        return primes.stream().takeWhile(p-> p < Math.sqrt(candidate)).noneMatch((p)->candidate%p==0);

    }*/

    @Override
    public Supplier< List<Integer>> supplier() {

        return ArrayList::new;


    }

    @Override
    public BiConsumer<List<Integer>, Integer> accumulator() {
        return (list,candidate) -> {

            if (list.size()==0)
            {
                list.add(0);
                return;

            }

            if (list.size()==1)
            {
                list.add(1);
                return;
            }

            int index = list.size()-1;
            int x= list.get(index);
            int y= list.get(index-1);
            list.add(x+y);


        };
    }

    @Override
    public BinaryOperator<List<Integer>> combiner() {
        return (list1,list2) -> {

           list1.addAll(list2);

           return list1;
        };



    }

    @Override
    public Function<List<Integer>, List<Integer>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
