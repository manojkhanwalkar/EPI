package java891011121314;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PrimeNumberCollector implements Collector<Integer,Map<Boolean, List<Integer>>, Map<Boolean,List<Integer>>> {





    public static Collector<Integer,Map<Boolean, List<Integer>>, Map<Boolean,List<Integer>>>  toList() {
        return new PrimeNumberCollector();
    }

    public static boolean  isPrime(List<Integer> primes, int candidate)
    {

        return primes.stream().takeWhile(p-> p < Math.sqrt(candidate)).noneMatch((p)->candidate%p==0);

    }

    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {

        return () -> {
            Map<Boolean, List<Integer>> map = new HashMap<>();

            map.put(true, new ArrayList<>());
            map.put(false, new ArrayList<>());

            return map;

        }; 


    }

    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (map,candidate) -> {
            List<Integer> primes = map.get(true);

            boolean result = isPrime(primes,candidate);

            map.get(result).add(candidate);


        };
    }

    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (map1,map2) -> {

            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));

            return map1;
        };



    }

    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
