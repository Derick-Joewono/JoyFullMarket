package controller;

import model.Product;
import repository.ProductRepository;
import java.util.List;

public class ProductController {

    ProductRepository repo = new ProductRepository();

    public List<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public boolean isStockAvailable(int id, int qty) {
        Product p = repo.getProductById(id);
        return p != null && p.getStock() >= qty;
    }

    public boolean reduceStock(int id, int qty) {
        return repo.reduceStock(id, qty);
    }
}
	