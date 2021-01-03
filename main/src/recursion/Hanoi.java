package recursion;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Hanoi {

    public static void main(String[] args) {

        Deque<Integer> S = new LinkedList<>();
        Deque<Integer> D = new LinkedList<>();
        Deque<Integer> I = new LinkedList<>();

        for (int i=1;i<=10;i++)
        {
            S.add(i);
        }


     //   S.stream().forEach(i->System.out.println(i));
        move(S,D,I,10);

        D.stream().forEach(i->System.out.println(i));

    }

    public static void move(Deque<Integer> S ,Deque<Integer> D, Deque<Integer> I, int num)
    {
        if (num>0)
        {
            move(S,I,D,num-1);
            int i = S.remove();
            D.addFirst(i);
            move(I,D,S,num-1);
        }
    }




}
