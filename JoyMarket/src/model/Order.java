package model;

public class Order {
    private int id;
    private int customerId;
    private double total;
    private String status;
    private Integer courierId;
    private String deliveryStatus;

    public Order(int id, int customerId, double total, String status, Integer courierId, String deliveryStatus) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.status = status;
        this.courierId = courierId;
        this.deliveryStatus = deliveryStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
