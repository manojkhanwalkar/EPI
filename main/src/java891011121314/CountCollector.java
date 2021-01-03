package java891011121314;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CountCollector implements Collector<Integer,Integer, Integer> {


    int count=0;
    @Override
    public Supplier<Integer> supplier() {
        return ()-> 0;
    }

    @Override
    public BiConsumer<Integer,Integer> accumulator() {
        return (x,y) -> { count = count+y ;};
    }

    @Override
    public BinaryOperator<Integer> combiner() {
        return (x,y) -> { x = x+y; return x; } ;
    }

    @Override
    public Function<Integer,Integer> finisher() {
        return (x)->count;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.EMPTY_SET;
    }
}
