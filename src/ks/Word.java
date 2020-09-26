package ks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Word {


    Map<Person,Integer> persons = new HashMap<>();


    String word;

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



    public Word(String word)
    {
        this.word=word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return word.equals(word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                '}';
    }
}
