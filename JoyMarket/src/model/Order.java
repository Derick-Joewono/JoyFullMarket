package model;

public class Order {
    private int orderId;
    private int customerId;
    private int courierId;
    private String status;

    public Order(int orderId, int customerId, int courierId, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.courierId = courierId;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public int getCustomerId() { return customerId; }
    public int getCourierId() { return courierId; }
    public String getStatus() { return status; }
}
