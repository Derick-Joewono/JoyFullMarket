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

    public Admin getCurrentAdmin() {
        return SessionManager.getInstance().getCurrentAdmin();
    }

    
    public boolean updateProduct(Product p) {
        try {
            return productRepo.updateProduct(p.getId(), p.getStock(), p.getPrice());
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

	public boolean addProduct(String name, double price, int stock) {
		try {
			System.out.println("Adding product: " + name + ", price: " + price + ", stock: " + stock);
			boolean result = productRepo.insertProduct(name, price, stock);
			System.out.println("Add product result: " + result);
			return result;
		} catch (Exception e) {
			System.err.println("Error in addProduct: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean increaseStock(int productId) {
		Product product = productRepo.getProductById(productId);
		if (product == null) return false;
		return productRepo.updateStock(productId, product.getStock() + 1);
	}

	public boolean decreaseStock(int productId) {
		Product product = productRepo.getProductById(productId);
		if (product == null) return false;
		int newStock = product.getStock() - 1;
		if (newStock < 0) newStock = 0;
		return productRepo.updateStock(productId, newStock);
	}

	public boolean adjustStock(int productId, int amount) {
		Product product = productRepo.getProductById(productId);
		if (product == null) return false;
		int newStock = product.getStock() + amount;
		if (newStock < 0) newStock = 0;
		return productRepo.updateStock(productId, newStock);
	}

	public boolean updatePrice(int productId, double newPrice) {
		try {
			return productRepo.updatePrice(productId, newPrice);
		} catch (Exception e) {
			return false;
		}
	}
}
