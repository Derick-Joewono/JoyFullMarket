package model;

public class CustomerBalance {
    private int customerId;
    private double balance;

    public CustomerBalance(int customerId, double balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    public int getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
