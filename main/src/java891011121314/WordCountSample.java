package java891011121314;

import java891011121314.phaserframework.MuteX;
import java891011121314.phaserframework.ReLock;
import java891011121314.phaserframework.Sema4;

import java.nio.CharBuffer;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCountSample {


    static class WordCounter
    {
       //AtomicInteger count = new AtomicInteger(0);
    //    boolean isSpace=false;

        int count=0;

        //MuteX mutex = new MuteX(1);

       // Sema4 mutex = new Sema4();

        Lock lock = new ReLock();

        public WordCounter()
        {

        }



        public  WordCounter accumulate(Character c)
        {
            if (Character.isSpaceChar(c)) {
                //count.incrementAndGet();

                lock.lock();
               // mutex.acquire();
                count++;
               // mutex.release();
                lock.unlock();
            }

            return this;
        }


        public WordCounter combine(WordCounter other)
        {

          //  System.out.println(this + " " + other);
           // return new WordCounter(this.count+other.count);

            return this;
        }




    }


    static class CharCounter
    {
        AtomicInteger count= new AtomicInteger(0);
        //    boolean isSpace=false;

        public CharCounter()
        {

        }



        public   CharCounter accumulate(Character c)
        {
       //     System.out.println(this + "  " + count);
                count.incrementAndGet();
                return this;
                //return new CharCounter(count);
        }


        public CharCounter combine(CharCounter other)
        {
            if (this!=other)
            {
                System.out.println(this + " " + other);
            }


            return this;
        }




    }




    static class WordCounterSpliterrator implements Spliterator<Character>
    {

        String str;
        int current=0;

        public WordCounterSpliterrator(String str)
        {
            this.str = str;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> consumer) {

            if (current < str.length()) {
                consumer.accept(str.charAt(current++));
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public Spliterator<Character> trySplit() {

            // if string length <10 return null;
            // split string in half and then position to white space

          //  System.out.println("try cplit called " + str.length() + str);

            int currentSize = str.length()-current;

            if (currentSize<15)
                return null;

            int mid = currentSize/2+current;

            for (int i=mid;i<str.length();i++)
            {
              if (Character.isSpaceChar(str.charAt(i))) {

                  if (i+1==str.length())
                      return null;
                  String other = str.substring(current,i+1);
                  current = i+1;
                  return new WordCounterSpliterrator(other);

              }


            }

            return null;





        }

        @Override
        public long estimateSize() {
            return str.length()-current;
        }

        @Override
        public int characteristics() {
            return ORDERED+SIZED+SUBSIZED+NONNULL+IMMUTABLE;
        }
    }




    static class CharCounterSpliterrator implements Spliterator<Character>
    {

        String str;
        volatile int current=0;

        public CharCounterSpliterrator(String str)
        {
            this.str = str;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> consumer) {

            if (current < str.length()) {
                consumer.accept(str.charAt(current++));
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public Spliterator<Character> trySplit() {

            // if string length <10 return null;
            // split string in half and then position to white space


            int currentSize = str.length()-current;

            if (currentSize<15)
                return null;

            int mid = currentSize/2+current;

            String other = str.substring(current,mid+1);

            current = mid+1;

           // System.out.println("Split strings are " + other + "===" + new String(str.substring(current)));

            return new CharCounterSpliterrator(other);

        }

        @Override
        public long estimateSize() {
            return str.length()-current;
        }

        @Override
        public int characteristics() {
            return ORDERED+SIZED+SUBSIZED+NONNULL+IMMUTABLE;
        }
    }

    static  String sample = "Hello World String to be used for parallel splitterator. It is single spaced to make it somewhat more convenient. ";



    public static void main(String[] args) {

        sample = sample+sample+sample;

     /*   Stream<Character> stream = IntStream.range(0,sample.length()).mapToObj(sample::charAt);

       // stream.forEach(System.out::println);

        List<String> words = stream.collect(new WordCollector());

        System.out.println(words);*/

       // Stream<Character> stream = IntStream.range(0,sample.length()).mapToObj(sample::charAt);


        //sample = sample+sample+sample+sample;

        Spliterator<Character> spliterator = new WordCounterSpliterrator(sample);

        Stream<Character> stream = StreamSupport.stream(spliterator,true);

        WordCounter counter = stream.reduce(new WordCounter(),WordCounter::accumulate,WordCounter::combine);

       // System.out.println(counter.count.get());

        System.out.println(counter.count);

        /*

        Spliterator<Character> spliterator = new CharCounterSpliterrator(sample);

        Stream<Character> stream = StreamSupport.stream(spliterator,true);

        CharCounter counter = stream.reduce(new CharCounter(),CharCounter::accumulate,CharCounter::combine);

        System.out.println(counter.count.get());

         */


    }


}
