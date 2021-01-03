package java891011121314.patterns;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Template<T,R> {

    Function<T,R> preFunction;
    Consumer<T> consumer ;


    public Template( Function<T,R> preFunction ,Consumer<T> consumer )
    {
        this.preFunction = preFunction;
        this.consumer = consumer;
    }

    public R preProcessing(T t ) {

        return preFunction.apply(t);
    }


    public void postProcessing(T t) {

        consumer.accept(t);
    }


    abstract void process(T t);


    static class StringTemplate extends Template<String,String> {

        public StringTemplate(Function<String,String> preFunction ,Consumer<String> consumer )
        {
            super(preFunction,consumer);
        }

        public void process(String str)
        {
            str = preProcessing(str);
           // System.out.println(str);
            postProcessing(str);
        }

    }

    public static void main(String[] args) {

        StringTemplate template1 = new StringTemplate((s) -> s.toLowerCase(), (s) -> System.out.println(s));
        template1.process("HELLO world");

        StringTemplate template12= new StringTemplate((s) -> s.toUpperCase(), (s) -> System.out.println(s));
        template12.process("HELLO world");



    }


}
