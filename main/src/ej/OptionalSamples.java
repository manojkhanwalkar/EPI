package ej;

import java.util.*;
import java.util.stream.Collectors;

public class OptionalSamples {



    public static<E extends Comparable<E>>  Optional<E> max(Collection<E> collection)
    {
        if (collection.isEmpty())
            return Optional.empty();

       E result = null;

       for (E e : collection)
       {
           if (result==null || e.compareTo(result)>0)
           {
               result = e;
           }
       }

       return Optional.of(result);


       // return collection.stream().max(Comparator.naturalOrder());
    }


    public static void main(String[] args) {


        max(List.of(1,5,6)).ifPresentOrElse(System.out::println, ()-> System.out.println("Not found "));


       var list =  List.of(1,2,3,4,5,6).stream().map(i->Optional.of(i)).collect(Collectors.toList());

       var list1 = list.stream().flatMap(Optional::stream).collect(Collectors.toList());

        System.out.println(list1);

    }

}
