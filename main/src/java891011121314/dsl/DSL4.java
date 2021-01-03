package java891011121314.dsl;

import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

import static java891011121314.dsl.DSL3.LambdaOrderBuilder.order;
import static java891011121314.dsl.DSL4.OrderBuilder.*;
import static java891011121314.dsl.Domain.*;
import static java891011121314.dsl.Domain.Trade.Type.BUY;
import static java891011121314.dsl.Domain.Trade.Type.SELL;

//import static java891011121314.dsl.DSL2.OrderBuilder.at;

public class DSL4 {

 /*   public static void main(String[] args) {


        Order order =  forCustomer("Cust1",

                buy(t->t.quantity(100).at(125).stock("IBM").on("NYSE"))



                );

            System.out.println(order);

    }
*/

    static class OrderBuilder
    {



        public static Order forCustomer(String customer, TradeBuilder... tradeBuilders)
        {
            final Order order = new Order();
            order.customer = customer;

            Stream.of(tradeBuilders).forEach(t -> order.addTrade(t.trade));


            return order;
        }


       public  static  TradeBuilder buy(Consumer<TradeBuilder> consumer)
        {

            TradeBuilder tradeBuilder = new TradeBuilder();

            tradeBuilder.type(BUY);
            consumer.accept(tradeBuilder);

            return tradeBuilder;


//            order.addTrade(tradeBuilder.trade);




        }

        public static TradeBuilder  sell(Consumer<TradeBuilder> consumer)
        {
            TradeBuilder tradeBuilder = new TradeBuilder();
            tradeBuilder.type(SELL);

            consumer.accept(tradeBuilder);

            return tradeBuilder;

         //   order.addTrade(tradeBuilder.trade);

        }








    }

    static class TradeBuilder
    {
        private final Trade trade = new Trade();

        public void type(Trade.Type type)
        {
            trade.type=type;
        }

        public TradeBuilder quantity(int qty)
        {
            trade.quantity=qty;

            return this;
        }



        public  TradeBuilder at(int price)
        {
            trade.price=price;
            return this;
        }


        public StockBuilder stock(String symbol)
        {
            StockBuilder stockBuilder = new StockBuilder();

            stockBuilder.symbol(symbol);

            trade.stock=stockBuilder.stock;
            return stockBuilder;


        }

    }


    static class StockBuilder
    {
        Stock stock = new Stock();

        public StockBuilder symbol(String s)
        {
            stock.symbol=s;
            return this;
        }

        public StockBuilder on(String s)
        {
            stock.market=s;
            return this;
        }



    }


    static class Tax
    {

        public double getFederal(double value)
        {
            System.out.println("Federal" + value);
            return value+0.2;
        }

        public double getState(double value)
        {
            System.out.println("State"+value);
            return value+0.1;
        }

        public double getLocal(double value)
        {
            System.out.println("Local"+value);
            return value+0.3;
        }



    }

    static class TaxCalculator
    {
        DoubleUnaryOperator fun = d->d;

        public TaxCalculator with(DoubleUnaryOperator f)
        {
            fun = fun.andThen(f);
            return this;
        }

        public double calculate(double value)
        {
            return fun.applyAsDouble(value);
        }
    }


    public static void main(String[] args) {

        Tax tax = new Tax();

        TaxCalculator calculator = new TaxCalculator();

        calculator.with(tax::getFederal)
                .with(tax::getLocal)
                .with(tax::getState);

        System.out.println(calculator.calculate(0)*100);

        System.out.println(calculator.fun);

    }


}
