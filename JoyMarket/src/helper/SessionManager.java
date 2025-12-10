package helper;

import model.Customer;
import model.Courier;

public class SessionManager {
    
    private static SessionManager instance;
    private Customer currentCustomer;
    private Courier currentCourier;
    
    private SessionManager() {
        // Private constructor for singleton
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // Customer session methods
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        this.currentCourier = null; // Clear courier session
    }
    
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public boolean isCustomerLoggedIn() {
        return currentCustomer != null;
    }
    
    public int getCurrentCustomerId() {
        return currentCustomer != null ? currentCustomer.getId() : -1;
    }
    
    public String getCurrentCustomerName() {
        return currentCustomer != null ? currentCustomer.getFull_name() : "Guest";
    }
    
    // Courier session methods
    public void setCurrentCourier(Courier courier) {
        this.currentCourier = courier;
        this.currentCustomer = null; // Clear customer session
    }
    
    public Courier getCurrentCourier() {
        return currentCourier;
    }
    
    public boolean isCourierLoggedIn() {
        return currentCourier != null;
    }
    
    public int getCurrentCourierId() {
        return currentCourier != null ? currentCourier.getCourierId() : -1;
    }
    
    public String getCurrentCourierName() {
        return currentCourier != null ? currentCourier.getName() : "Guest";
    }
    
    // General methods
    public boolean isLoggedIn() {
        return currentCustomer != null || currentCourier != null;
    }
    
    public void logout() {
        this.currentCustomer = null;
        this.currentCourier = null;
    }
}
