package java891011121314.dsl;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static java891011121314.dsl.DSL2.OrderBuilder.*;
import static java891011121314.dsl.Domain.*;
import static java891011121314.dsl.Domain.Trade.Type.BUY;
import static java891011121314.dsl.Domain.Trade.Type.SELL;

//import static java891011121314.dsl.DSL2.OrderBuilder.at;

import static java891011121314.dsl.DSL3.LambdaOrderBuilder.*;

public class DSL3 {

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


/*Order order = order("Cust1",
              buy(100,
              stock("IBM", on("NYSE")),
              at(125)),

              sell(200,
                      stock("MSFT", on("NASDAQ")), at(225))


);*/

        Order order =  order(o->
        {

            o.forCustomer("Cust1");
            o.buy(t-> {

                t.price(125);
                t.quantity(100);
                t.stock(s-> {

                    s.market("NYSE");
                    s.symbol("IBM");

                        }
                );
            });

            o.sell(t-> {

                t.price(125);
                t.quantity(100);
                t.stock(s-> {

                            s.market("NYSE");
                            s.symbol("IBM");

                        }
                );
            });


        });


            System.out.println(order);

    }


    static class LambdaOrderBuilder
    {

        final Order order = new Order();



        public static Order order(Consumer<LambdaOrderBuilder> consumer)
        {
            LambdaOrderBuilder orderBuilder = new LambdaOrderBuilder();
            consumer.accept(orderBuilder);


           return orderBuilder.order;
        }

        public void forCustomer(String customer)
        {
            order.customer = customer;
        }


        public  void buy(Consumer<TradeBuilder> consumer)
        {

            TradeBuilder tradeBuilder = new TradeBuilder();

            tradeBuilder.type(BUY);
            consumer.accept(tradeBuilder);

            order.addTrade(tradeBuilder.trade);






        }

        public  void sell(Consumer<TradeBuilder> consumer)
        {
            TradeBuilder tradeBuilder = new TradeBuilder();
            tradeBuilder.type(SELL);

            consumer.accept(tradeBuilder);

            order.addTrade(tradeBuilder.trade);

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

    static class TradeBuilder
    {
        private final Trade trade = new Trade();

        public void type(Trade.Type type)
        {
            trade.type=type;
        }

        public void quantity(int qty)
        {
            trade.quantity=qty;
        }

        public void price(int p)
        {
            trade.price = p;
        }

        public void stock(Consumer<StockBuilder> consumer)
        {
            StockBuilder stockBuilder = new StockBuilder();

            consumer.accept(stockBuilder);

            trade.stock = stockBuilder.stock;


        }

    }


    static class StockBuilder
    {
        Stock stock = new Stock();

        public void symbol(String s)
        {
            stock.symbol=s;
        }

        public void market(String s)
        {
            stock.market=s;
        }



    }







}
