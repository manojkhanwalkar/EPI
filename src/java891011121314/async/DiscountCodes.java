package java891011121314.async;

import java.util.*;

public class DiscountCodes {


    public int getDiscountPercentage(String shop, String item)
    {

            delay();
            Integer discount = discounts.get(new Tuple(shop,item));

            if (discount!=null)
                return discount;
            else
                return 0;
    }


    private void delay()
    {
        int time =  1000;

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   static  Map<Tuple,Integer> discounts = new HashMap<>();

    public static void main(String[] args) {

        discounts.put(new Tuple("shop1", "item1"), 10);
        discounts.put(new Tuple("shop2", "item1"), 1);
        discounts.put(new Tuple("shop3", "item1"), 5);


    }

}
