package java891011121314.async;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceTester {

    public static void main(String[] args) {

        PriceTester pricetester = new PriceTester();

        Shop shop1 = new ShopFront("shop1");
        Shop shop2 = new ShopFront("shop2");
        Shop shop3 = new ShopFront("shop3");

        List<Shop> shops = List.of(shop1,shop2,shop3);

   /*     int best = pricetester.getBestPriceSequentially(shops);

        System.out.println(best);

        best = pricetester.getBestPriceSequentiallyStream(shops);

        System.out.println(best);



        best = pricetester.getBestPriceParallelStream(shops);

        System.out.println(best);*/


        int best = pricetester.getBestPriceCompletableFuture(shops);

        System.out.println("Using Completable Future   " + best);





    }


  /*  public int getBestPriceSequentiallyStream(List<Shop> shops) {


        return shops.stream().map((s)->s.getPrice("Item")).sorted().findFirst().get();
    }


    public int getBestPriceParallelStream(List<Shop> shops) {


        return shops.parallelStream().map((s)->s.getPrice("Item")).sorted().findFirst().get();
    }*/

    public int getBestPriceCompletableFuture(List<Shop> shops) {

        // first get a discount and as soon as you get that get a price for that item - once you get all prices , get the least price

        DiscountCodes discountCodes = new DiscountCodes();

        Map<Tuple,Integer> discounts = new ConcurrentHashMap<>();

        Set<Tuple> shoptitem = Set.of(new Tuple("shop1","item1"),new Tuple("shop2","item1"), new Tuple("shop3","item1") );


        var priceCollectors = shops.stream()
                .map((si)->CompletableFuture.supplyAsync(()-> {


                    int disc = discountCodes.getDiscountPercentage(si.getName(),"item1");
                    discounts.put(new Tuple(si.getName(),"item1"),disc);
                    return si;

                }).thenCompose((s)->CompletableFuture.supplyAsync(()->s.getPrice("item1",discounts.get(new Tuple(s.getName(),"item1")))))

                .orTimeout(3,TimeUnit.SECONDS).thenCombine(CompletableFuture.supplyAsync(()->70),(price,conv)->price*conv).orTimeout(3, TimeUnit.SECONDS)
                )
                .collect(Collectors.toList());



    /*    CompletableFuture<Integer> []  tmp =  new CompletableFuture[priceCollectors.size()];

        for (int i=0;i<priceCollectors.size();i++)
        {
            tmp[i] = priceCollectors.get(i);
        }

        CompletableFuture.allOf(tmp).join();*/


        return priceCollectors.stream().map((f)->f.join()).sorted().findFirst().get();

        //return shops.parallelStream().map((s)->s.getPrice("Item")).sorted().findFirst().get();
    }



  /*  public int getBestPriceCompletableFuture(List<Shop> shops) {

        DiscountCodes discountCodes = new DiscountCodes();

        Map<Tuple,Integer> discounts = new ConcurrentHashMap<>();

        List<Tuple> shoptitem = List.of(new Tuple("shop1","item1"),new Tuple("shop2","item1"), new Tuple("shop3","item1") );

        shoptitem.parallelStream().forEach((si)->{

            int disc = discountCodes.getDiscountPercentage(si.shop,si.item);
            discounts.put(si,disc);});

        var priceCollectors = shops.stream().map((s)->CompletableFuture.supplyAsync(()->s.getPrice("item1",discounts.get(new Tuple(s.getName(),"item1"))))).collect(Collectors.toList());

        return priceCollectors.stream().map((f)->f.join()).sorted().findFirst().get();

        //return shops.parallelStream().map((s)->s.getPrice("Item")).sorted().findFirst().get();
    } */


    /*
      public int getBestPriceCompletableFuture(List<Shop> shops) {



        var priceCollectors = shops.stream().map((s)->CompletableFuture.supplyAsync(()->s.getPrice("item1"))).collect(Collectors.toList());

        return priceCollectors.stream().map((f)->f.join()).sorted().findFirst().get();

        //return shops.parallelStream().map((s)->s.getPrice("Item")).sorted().findFirst().get();
    }

     */

/*

    public int getBestPriceSequentially(List<Shop> shops)
    {
            int best = Integer.MAX_VALUE;

            for (Shop shop : shops)
            {
                int price = shop.getPrice("Item");
                best =  price < best ? price : best;
            }

            return best;
    }
*/
}
