package java891011121314.hashes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseHashSet<T> {

    public abstract void acquire(T t);

    public abstract void release(T t);

    List<T>[] elements;

    public BaseHashSet(int capacity)
    {
        elements = (List<T>[]) new List[capacity];

        for (int i=0;i<elements.length;i++)
        {
            elements[i]= new ArrayList<>();
        }
    }

    public void put(T t )
    {
        try {
            acquire(t);

            int bucket = Math.abs(t.hashCode()%elements.length);
            if (!contains(t)) {
                elements[bucket].add(t);
                size++;
            }


        } finally {

            release(t);

        }

        if (policy())
            resize();
    }

    protected abstract boolean policy();

    protected int size = 0;


 /*   public T get()
    {
        try {
            acquire(t);

        } finally {

            release(t);

        }
    }*/

    public boolean contains(T t)
    {
        try {
            acquire(t);
            int bucket = Math.abs(t.hashCode()%elements.length);
            List<T> list = elements[bucket];

            return  list.contains(t);


        } finally {

            release(t);

        }
    }

    protected abstract void resize();

    public void print()
    {
        Arrays.stream(elements).flatMap(element->element.stream()).sorted().forEach(System.out::println);

    }


}
