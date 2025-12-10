package model;

public class CartItem {
	private int cartId;
	private int productId;
	private String productName;
	private double price;
	private int quantity;

	public CartItem(int cartId, int productId, String productName, double price, int quantity) {
		this.cartId = cartId;
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
	}

	public int getCartId() {
		return cartId;
	}

	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int q) {
		this.quantity = q;
	}

	@Override
	public String toString() {
		return productName + " | Qty: " + quantity + " | Rp " + price;
	}
}
