package java891011121314.flow;

public class Order {

    public String symbol;
    public String orderId;

    public int qty;

    public int price;

    @Override
    public String toString() {
        return "Order{" +
                "symbol='" + symbol + '\'' +
                ", orderId='" + orderId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }


    public Order(String symbol, String orderId, int qty, int price) {
        this.symbol = symbol;
        this.orderId = orderId;
        this.qty = qty;
        this.price = price;
    }
}
