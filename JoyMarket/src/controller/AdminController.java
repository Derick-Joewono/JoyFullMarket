package controller;

import repository.ProductRepository;

import java.util.List;

import helper.SessionManager;
import model.Admin;
import model.Courier;
import model.Order;
import model.Product;
import repository.AdminRepository;
import repository.CourierRepository;
import repository.OrderRepository;

public class AdminController {

    private ProductRepository productRepo = new ProductRepository();
    private CourierRepository courierRepo = new CourierRepository();
    private OrderRepository orderRepo = new OrderRepository();
    private AdminRepository adminRepo = new AdminRepository();

    public boolean login(String email, String password) {
        Admin admin = adminRepo.getAdminByEmail(email);

        if (admin == null) {
            return false;
        }

        // Verify password (plain text comparison for simplicity)
        if (password.equals(admin.getPassword())) {
            SessionManager.getInstance().setCurrentAdmin(admin);
            return true;
        }

        return false;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    
    public boolean updateProduct(Product p) {
		
    	try {
			return productRepo.updateStock(p.getId(), p.getStock());
		} catch (Exception e) {
			return false;
		}
   
    }

    public boolean assignCourier(int orderId, int courierId) {
        boolean success = orderRepo.assignCourier(orderId, courierId);

        if (success) return true;
        else return false;
    }

    public List<Product> listProducts() {
        return productRepo.getAllProducts();
    }

    public List<Courier> listCouriers() {
        return courierRepo.getAllCouriers();
    }

	public List<Order> listOpenOrders() {
		return orderRepo.getAllOrders();
		
	}
}
