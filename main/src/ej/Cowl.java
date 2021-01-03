package ej;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cowl<T> implements Iterable<T>{

    Lock lock = new ReentrantLock();


    public void add(T t )
    {
        try {
            lock.lock();
            T[] newcontents =  (T[])new Object[contents.length+1];
            System.arraycopy(contents,0,newcontents,0,contents.length);
            newcontents[contents.length] = t;

            System.out.println(contents);
            contents = newcontents;

            System.out.println(newcontents);

        } finally {
            lock.unlock();
        }

    }

    public T get(int index)
    {
        return contents[index];
    }


    volatile T[] contents;
    public Cowl()
    {
        this.contents = (T[])new Object[0];
    }

    @Override
    public Iterator<T> iterator() {
        return new CowlIterator<>(this);
    }


    static class CowlIterator<T> implements Iterator<T>
    {
        private T[] contents ;

        int index=0;

        public CowlIterator(Cowl<T> list)
        {
            this.contents = list.contents;
        }
        @Override
        public boolean hasNext() {
            return index<contents.length;
        }

        @Override
        public T next() {
            return contents[index++];
        }
    }


    public static void main(String[] args) {

        Cowl<Integer> list = new Cowl<>();

        list.add(0);

        Iterator<Integer> iterator = list.iterator();

        list.add(1);
        list.add(2);


        while(iterator.hasNext())
        {
            System.out.println(iterator.next());
        }


    }

}
