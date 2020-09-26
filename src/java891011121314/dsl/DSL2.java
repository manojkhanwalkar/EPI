package java891011121314.dsl;

import java.util.List;
import java.util.stream.Stream;

import static java891011121314.dsl.DSL1.OrderBuilder.forCustomer;
//import static java891011121314.dsl.DSL2.OrderBuilder.at;
import static java891011121314.dsl.DSL2.OrderBuilder.*;
import static java891011121314.dsl.Domain.*;
import static java891011121314.dsl.Domain.Trade.Type.BUY;
import static java891011121314.dsl.Domain.Trade.Type.SELL;

public class DSL2 {

    public static void main(String[] args) {

/*            Order order = forCustomer("Cust1")
                                 .buy(100)
                                 .stock("IBM")
                                  . on("NYSE")
                                  .at(125)
                    .sell(200)
                    .stock("MSFT")
                    . on("NASDAQ")
                    .at(225)

                    .end();*/


Order order = order("Cust1",
              buy(100,
              stock("IBM", on("NYSE")),
              at(125)),

              sell(200,
                      stock("MSFT", on("NASDAQ")), at(225))


);


            System.out.println(order);

    }


    static class OrderBuilder
    {




        public static Order order(String customer, Trade... trades)
        {
            final Order order = new Order();
            order.customer=customer;
            Stream.of(trades).forEach(order::addTrade);

           return order;
        }


        public static Trade buy(int quantity, Stock stock, int price)
        {
            Trade trade = new Trade();
            trade.quantity=quantity;
            trade.type=BUY;
            trade.price=price;
            trade.stock=stock;

            return trade;



        }

        public static Trade sell(int quantity, Stock stock , int price)
        {
            Trade trade = new Trade();
            trade.quantity=quantity;
            trade.type=SELL;
            trade.price=price;
            trade.stock= stock;

            return trade;

        }


        public static Stock stock(String symbol, String market)
        {
            Stock stock = new Stock();

            stock.symbol=symbol;
            stock.market=market;

            return stock;
        }

        public static int at(int price)
        {
            return price;
        }

        public static String on(String market)
        {
            return market;
        }

       // public static Stock stock()




    }






}
