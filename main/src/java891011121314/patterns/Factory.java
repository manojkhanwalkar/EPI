package java891011121314.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Factory {

    static abstract class Product
    {


    }

    static Map<String, Supplier<Product>> maps = new HashMap<>();

    static {

        maps.put("Loan" , Loan::new);
        maps.put("Checking" , Checkng::new);
        maps.put("Savings" , Savings::new);


    }

    public static Product get(String name)
    {

        Supplier<Product> supplier = maps.get(name);

        return supplier.get();
    }

    static class Loan extends Product
    {

    }

    static class Savings extends Product
    {

    }

    static class Checkng extends Product
    {

    }


    public static void main(String[] args) {


        Loan loan = (Loan)Factory.get("Loan");
        System.out.println(loan);


    }

}
