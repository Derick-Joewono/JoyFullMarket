package helper;

import model.Customer;
import model.Admin;
import model.Courier;

public class SessionManager {
    
    private static SessionManager instance;
    private Customer currentCustomer;
    private Courier currentCourier;
    private Admin currentAdmin;
    
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
        this.currentAdmin = null; // Clear admin session
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
        this.currentAdmin=null; // Clear admin session
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
    
    // Admin session methods
    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
        this.currentCustomer = null; // Clear customer session
        this.currentCourier=null; // Clear courier session
    }
    
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
    
    public boolean isAdminLoggedIn() {
        return currentAdmin != null;
    }
    
    public int getCurrentAdminId() {
        return currentAdmin != null ? currentAdmin.getAdminId() : -1;
    }
    
    public String getCurrentAdminName() {
        return currentAdmin != null ? currentAdmin.getName() : "Guest";
    }
    
    // General methods
    public boolean isLoggedIn() {
        return currentCustomer != null || currentCourier != null || currentAdmin !=null;
    }
    
    public void logout() {
        this.currentCustomer = null;
        this.currentCourier = null;
        this.currentAdmin =null;
    }
}
