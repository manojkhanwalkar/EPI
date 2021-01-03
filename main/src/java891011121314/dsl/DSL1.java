package java891011121314.dsl;

import java.util.ArrayList;
import java.util.List;

import static java891011121314.dsl.DSL1.OrderBuilder.forCustomer;
import static java891011121314.dsl.Domain.*;

public class DSL1 {

    public static void main(String[] args) {

            Domain.Order order = forCustomer("Cust1")
                                 .buy(100)
                                 .stock("IBM")
                                  . on("NYSE")
                                  .at(125)
                    .sell(200)
                    .stock("MSFT")
                    . on("NASDAQ")
                    .at(225)

                    .end();

            System.out.println(order);

    }


    static class OrderBuilder
    {
        final Order order = new Order();

        private OrderBuilder(String customer)
        {
            order.customer=customer;
        }

        public static OrderBuilder forCustomer(String customer)
        {
           return new OrderBuilder(customer);
        }


        public TradeBuilder buy(int quantity)
        {
            return new TradeBuilder(this, Trade.Type.BUY, quantity);
        }

        public TradeBuilder sell(int quantity)
        {
            return new TradeBuilder(this, Trade.Type.SELL, quantity);
        }



        public Order end()
        {
            return order;
        }

        public void add(Trade trade) {

            order.trades.add(trade);
        }
    }


    static class StockBuilder
    {
        Stock stock = new Stock();
        TradeBuilder tradeBuilder;

        public StockBuilder(String name, TradeBuilder tradeBuilder)
        {
            stock.symbol = name;
            this.tradeBuilder = tradeBuilder;
        }


        public TradeBuilder on(String market)
        {
            stock.market = market;
            tradeBuilder.trade.stock=stock;
            return tradeBuilder;
        }

    }


    static class TradeBuilder
    {
        private final Trade trade = new Trade();
        OrderBuilder orderBuilder;

        public TradeBuilder(OrderBuilder orderBuilder, Trade.Type type, int quantity) {

            this.orderBuilder=orderBuilder;
            trade.type=type;
            trade.quantity=quantity;
        }

        public StockBuilder stock(String name)
        {
            return new StockBuilder(name,this);
        }

        public OrderBuilder at(int price)
        {
            trade.price = price;
            orderBuilder.add(trade);
            return orderBuilder;
        }


    }




}
