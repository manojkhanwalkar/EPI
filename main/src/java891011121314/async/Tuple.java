package java891011121314.async;

import java.util.Objects;

public class Tuple
{
    String shop ;
    String item ;
    //int discount;

    public Tuple(String shop, String item) {
        this.shop = shop;
        this.item = item;
       // this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return shop.equals(tuple.shop) &&
                item.equals(tuple.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shop, item);
    }
}
