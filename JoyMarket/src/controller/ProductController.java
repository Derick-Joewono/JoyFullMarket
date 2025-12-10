package controller;

import java.util.List;

import model.Product;
import repository.ProductRepository;

public class ProductController {

	ProductRepository productRepo = new ProductRepository();
	
	public List<Product> getAllProducts() {

		return productRepo.getAllProducts();
	}

}
