package sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Duplicates {

    static class Person implements Comparable<Person> {

        String first;
        String last;


        public Person(String first,String last)
        {
            this.first = first;
            this.last = last;
        }

        @Override
        public int compareTo(Person person) {

            return this.first.compareTo(person.first);


        }

        @Override
        public String toString() {
            return "Person{" +
                    "first='" + first + '\'' +
                    ", last='" + last + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("Ian","Botham"));
        persons.add(new Person("David","Gower"));
        persons.add(new Person("Ian","Bell"));
        persons.add(new Person("Ian","Chappel"));

        Collections.sort(persons);

        int next=0;
        for (int i=1;i<persons.size();i++)
        {
            if (persons.get(next).first.equals(persons.get(i).first)) {
                // do nothing
            }
            else
            {
                next++;
                persons.set(next,persons.get(i));
            }

        }

        persons = persons.subList(0,next+1);

        System.out.println(persons);

    }
}
