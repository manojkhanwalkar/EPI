package java891011121314;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class WordCollector implements Collector<Character, List<String>, List<String>> {



    StringBuilder builder = new StringBuilder();

    @Override
    public Supplier<List<String>> supplier() {
        return ()-> { return new ArrayList<>(); };
    }

    @Override
    public BiConsumer<List<String>,Character> accumulator() {
        return (words,c) -> {

            if (Character.isSpaceChar(c))
            {
                words.add(builder.toString());
                builder = new StringBuilder();
            }
            else
            {
                builder.append(c);
            }

        };
    }

    @Override
    public BinaryOperator<List<String>> combiner() {
        return (words1,words2) -> { words1.addAll(words2);
                    return words1;
        } ;
    }

    @Override
    public Function<List<String>,List<String>> finisher() {
        return (words) -> {

            words.add(builder.toString());
            return words;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.EMPTY_SET;
    }
}
