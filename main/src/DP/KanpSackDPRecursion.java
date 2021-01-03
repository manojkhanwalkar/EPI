package DP;

import java.util.ArrayList;
import java.util.List;

public class KanpSackDPRecursion {

    static class Item
    {
        int value;
        int weight;


        public Item(int weight, int value)
        {
            this.weight = weight;
            this.value = value;
        }


    }


    public static void main(String[] args) {


        List<Item> items = new ArrayList<>();

        items.add(new Item(5,60));
        items.add(new Item(3,50));
        items.add(new Item(4,70));
        items.add(new Item(2,30));

        int[][] V = new int[items.size()][6];

        System.out.println(computeOptimum(items,V,items.size()-1,5));

    }


    public static int computeOptimum(List<Item> items, int[][] V , int i , int w)
    {
        if (i<0)
            return 0;

        int with ;

        if (w<items.get(i).weight)
            with = 0;
        else
        {
            with = computeOptimum(items,V,i-1,w-items.get(i).weight) + items.get(i).value;
        }

        int without = computeOptimum(items,V,i-1,w);

        V[i][w] = Math.max(with,without);

        return V[i][w];
    }


}
