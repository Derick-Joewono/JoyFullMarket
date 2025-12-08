package helper;

import model.Customer;

public class SessionManager {
    
    private static SessionManager instance;
    private Customer currentCustomer;
    
    private SessionManager() {
        // Private constructor for singleton
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }
    
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public boolean isLoggedIn() {
        return currentCustomer != null;
    }
    
    public void logout() {
        this.currentCustomer = null;
    }
    
    public int getCurrentCustomerId() {
        return currentCustomer != null ? currentCustomer.getId() : -1;
    }
    
    public String getCurrentCustomerName() {
        return currentCustomer != null ? currentCustomer.getFull_name() : "Guest";
    }
}






