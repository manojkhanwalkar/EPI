package java891011121314;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamOps {

    static class Trader
    {
        String name;
        String city;

        public Trader(String name, String city) {
            this.name = name;
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "Trader{" +
                    "name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }


    static class Transaction
    {
        Trader trader;
        int year;
        int value;

        public Transaction(Trader trader, int year, int value) {
            this.trader = trader;
            this.year = year;
            this.value = value;
        }


        public Trader getTrader() {
            return trader;
        }

        public void setTrader(Trader trader) {
            this.trader = trader;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return "Transaction{" +
                    "trader=" + trader +
                    ", year=" + year +
                    ", value=" + value +
                    '}';
        }
    }


    public static void main(String[] args) {

        Trader trader1 = new Trader("T1","C1");
        Trader trader2 = new Trader("T2","C2");
        Trader trader3 = new Trader("T3","C1");
        Trader trader4 = new Trader("T4","C1");

        List<Transaction> transactions = Arrays.asList(

                new Transaction(trader4,2011,300),
                new Transaction(trader1,2012,1000),
                new Transaction(trader1,2011,400),
                new Transaction(trader2,2012,710),
                new Transaction(trader2,2012,700),
                new Transaction(trader3,2012,950)




                );


 /*       List<Transaction> result1 = transactions.stream().filter(t->t.getYear()==2011).sorted(Comparator.comparingInt(Transaction::getValue)).collect(Collectors.toList());

        System.out.println(result1);

*/

   /*     List<String> cities = transactions.stream().map(t->t.getTrader().getCity()).distinct().collect(Collectors.toList());

        System.out.println(cities); */

  /*     List<Trader> traders = transactions.stream().map(t->t.getTrader()).filter(t->t.getCity().equals("C1")).sorted(Comparator.comparing(Trader::getName)).distinct().collect(Collectors.toList());

        System.out.println(traders);*/

 /* List<String> names = transactions.stream().map(t->t.getTrader().getName()).distinct().sorted().collect(Collectors.toList());

  System.out.println(names);  */

 /*       Optional<Trader> C2based = transactions.stream().map(t->t.getTrader()).filter(t->t.getCity().equals("2")).findAny();

        C2based.ifPresentOrElse(System.out::println, ()-> System.out.println("Not present"));*/

      /*  List<Integer> values = transactions.stream().filter(t->t.getTrader().getCity().equals("C1")).map(t->t.getValue()).collect(Collectors.toList());

        System.out.println(values); */

    //  Optional<Integer> max = transactions.stream().map(t->t.getValue()).sorted((t1,t2)->  t2-t1).findFirst();

        Optional<Integer> max = transactions.stream().map(t->t.getValue()).max((t1,t2)-> t1-t2);

        Optional<Integer> min = transactions.stream().map(t->t.getValue()).min((t1,t2)-> t1-t2);

      System.out.println(max.get());

        System.out.println(min.get());

        IntStream.range(0,100).forEach(i->{

            // stream style for loop


        });

    }

}
