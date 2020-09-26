package java891011121314;

import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class OddEvenCollector implements Collector<Integer,Map<OddEvenCollector.Type, List<Integer>>, Map<OddEvenCollector.Type,List<Integer>>> {

   public enum Type { Even , Odd};



    public static Collector<Integer,Map<OddEvenCollector.Type, List<Integer>>, Map<OddEvenCollector.Type,List<Integer>>>  toCollect() {
        return new OddEvenCollector();
    }

    public static boolean  isEven( int candidate)
    {

        return candidate%2==0;

    }



    @Override
    public Supplier<Map<OddEvenCollector.Type, List<Integer>>> supplier() {

        return () -> {
            Map<Type, List<Integer>> map = new HashMap<>();

            map.put(Type.Even, new ArrayList<>());
            map.put(Type.Odd, new ArrayList<>());

            return map;

        }; 


    }

    @Override
    public BiConsumer<Map<OddEvenCollector.Type, List<Integer>>, Integer> accumulator() {
        return (map,candidate) -> {

            boolean result = isEven(candidate);

            if (result)
                    map.get(Type.Even).add(candidate);
            else
                map.get(Type.Odd).add(candidate);


        };
    }

    @Override
    public BinaryOperator<Map<OddEvenCollector.Type, List<Integer>>> combiner() {

       // System.out.println("Combiner function retrieved");
        return (map1,map2) -> {

       //     System.out.println("Combiner function code called");

            map1.get(Type.Even).addAll(map2.get(Type.Even));
            map1.get(Type.Odd).addAll(map2.get(Type.Odd));

            return map1;
        };



    }

    @Override
    public Function<Map<OddEvenCollector.Type, List<Integer>>, Map<OddEvenCollector.Type, List<Integer>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
