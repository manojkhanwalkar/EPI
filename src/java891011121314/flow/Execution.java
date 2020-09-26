package java891011121314.flow;

public class Execution {

    String execId;
    String orderId;
    int price;
    int qty;

    @Override
    public String toString() {
        return "Execution{" +
                "execId='" + execId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }


    public Execution(String execId, String orderId, int price, int qty) {
        this.execId = execId;
        this.orderId = orderId;
        this.price = price;
        this.qty = qty;
    }
}
