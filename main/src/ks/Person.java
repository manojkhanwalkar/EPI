package ks;

import java.util.*;

public class Person {

Map<Person,Integer> persons = new HashMap<>();

Map<Word,Integer> words = new HashMap<>();

String emailid;

public Person(String emailid)
{
    this.emailid=emailid;
}

public void add(Person person)
{
    Integer freq = persons.get(person);

    if (freq==null)
    {
        persons.put(person,1);
    }
    else
    {
        persons.put(person, freq+1);
    }

}

    public void add(Word word)
    {
        Integer freq = words.get(word);

        if (freq==null)
        {
            words.put(word,1);
        }
        else
        {
            words.put(word, freq+1);
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return emailid.equals(person.emailid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailid);
    }

    @Override
    public String toString() {
        return "Person{" +
                "emailid='" + emailid + '\'' +
                '}';
    }
}
