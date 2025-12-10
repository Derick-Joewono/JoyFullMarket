package model;

public class Cart {
	private int id;
	private int customerId;
	private int productId;
	private int quantity;

	public Cart(int id, int customerId, int productId, int quantity) {
		this.id = id;
		this.customerId = customerId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public int getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
