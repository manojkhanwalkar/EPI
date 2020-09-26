package java891011121314.patterns;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Chain {


    public static void main(String[] args) {


        UnaryOperator<String> lower = (String str) -> { return str.toLowerCase(); };

        UnaryOperator<String> checker = (String str) -> { return str.trim();};

        Function<String,String> process = lower.andThen(checker);

        String result = process.apply("    Hello World   ");

        System.out.println(result);


    }
}
