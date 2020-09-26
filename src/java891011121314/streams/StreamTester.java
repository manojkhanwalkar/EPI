package java891011121314.streams;

import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class StreamTester {


    public static void main(String[] args) {

       Stream<Dish> menuStearm = Dish.menu().stream();

       StreamForker<Dish> streamForker = new StreamForker<>(menuStearm);

       Results result = streamForker.fork("Consolidated Menu" , s->s.map(Dish::getName).collect(joining(", ")))
               .fork("Total Calories ", s->s.mapToInt(Dish::getCalories).sum())
               .fork("Max Calories ", s->s.mapToInt(Dish::getCalories).max().getAsInt())
               .fork("Dishes by Type ", s->s.collect(groupingBy(Dish::getType))).getResults();


      // var result = menuStearm.map(m->m.getName()).collect(joining(", "));

       // var result = menuStearm.mapToInt(d->d.getCalories()).sum();


   //     var result = menuStearm.mapToInt(Dish::getCalories).max().getAsInt();

      //  var result = menuStearm.collect(groupingBy(Dish::getType));

       System.out.println(((ForkingStreamConsumer)result).get("Consolidated Menu"));


    }
}
