package java891011121314.dsl;

import java.util.ArrayList;
import java.util.List;

public class Domain {

    static class Stock
    {
        String symbol;
        String market;

        @Override
        public String toString() {
            return "Stock{" +
                    "symbol='" + symbol + '\'' +
                    ", market='" + market + '\'' +
                    '}';
        }
    }

    static class Trade
    {
        public enum Type { BUY,SELL};

        Stock stock ;
        int price;
        int quantity;
        Type type;

        @Override
        public String toString() {
            return "Trade{" +
                    "stock=" + stock +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", type=" + type +
                    '}';
        }
    }


    static class Order {
        String customer;
        List<Trade> trades = new ArrayList<>();

        public void addTrade(Trade trade)
        {
            trades.add(trade);
        }

        @Override
        public String toString() {
            return "Order{" +
                    "customer='" + customer + '\'' +
                    ", trades=" + trades +
                    '}';
        }
    }


}
