package java891011121314.async;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShopFront implements Shop {


    private final String name;
    Random random = new Random();

    Map<String,Integer> itemPrices = new HashMap<>();


    public ShopFront(String name)
    {
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    public synchronized int getPrice(String item, int discount)
    {
        delay();

        int price= itemPrices.computeIfAbsent(item,(k)-> random.nextInt(100));

        price = price-(discount*price)/100;

        return price;


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
}
