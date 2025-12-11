package controller;

import model.CartItem;
import model.Order;
import repository.CartRepository;
import repository.CustomerBalanceRepository;
import repository.OrderDetailRepository;
import repository.OrderRepository;
import repository.ProductRepository;

import java.util.List;

public class CartController {

    private final CartRepository cartRepo = new CartRepository();
    private final CustomerBalanceRepository balanceRepo = new CustomerBalanceRepository();
    private final ProductRepository productRepo = new ProductRepository();
    private final OrderRepository orderRepo = new OrderRepository();
    private final OrderDetailRepository orderDetailRepo = new OrderDetailRepository();

    // Add to cart: controller checks stock via productRepo then insert into cart
    public boolean addToCart(int customerId, int productId, int qty) {
        if (!productRepo.isStockAvailable(productId, qty)) return false;
        return cartRepo.addOrUpdateCart(customerId, productId, qty);
    }

    // Update quantity of a cart item
    public boolean updateCartQuantity(int cartId, int qty) {
        Integer productId = cartRepo.getProductIdByCartId(cartId);
        if (productId == null) return false;
        if (!productRepo.isStockAvailable(productId, qty)) return false;
        return cartRepo.updateQuantity(cartId, qty);
    }

    // Remove item
    public boolean removeFromCart(int cartId) {
        return cartRepo.deleteCartItem(cartId);
    }

    // Get cart items for UI (CartItem DTO)
    public List<CartItem> getCartItems(int customerId) {
        return cartRepo.getCartItemsByCustomer(customerId);
    }

    // Checkout: compute total, check balance, deduct balance, reduce stock per product, insert order & order_detail, clear cart
    public boolean checkout(int customerId) {
        List<CartItem> items = cartRepo.getCartItemsByCustomer(customerId);
        if (items == null || items.isEmpty()) return false;

        double total = 0;
        for (CartItem it : items) total += it.getPrice() * it.getQuantity();

        double balance = balanceRepo.getBalance(customerId);
        if (balance < total) return false;

        // Deduct balance
        boolean balOk = balanceRepo.topUp(customerId, -total);
        if (!balOk) return false;

        // Reduce stock for each item (non-transactional; for production use transaction)
        for (CartItem it : items) {
            boolean ok = productRepo.reduceStock(it.getProductId(), it.getQuantity());
            if (!ok) {
                // NOTE: if a reduceStock fails here after balance deducted, DB inconsistent.
                // For production, wrap in transaction. For now, we assume reduceStock succeeds.
                return false;
            }
        }

        // Insert order (courier_id is NULL, delivery_status is PENDING)
        Order order = new Order(0, customerId, total, "PENDING", null, "PENDING");
        int orderId = orderRepo.insertOrderAndGetId(order);
        if (orderId <= 0) {
            return false;
        }

        // Insert order_detail for each item
        for (CartItem it : items) {
            double subtotal = it.getPrice() * it.getQuantity();
            boolean ok = orderDetailRepo.insertOrderDetail(orderId, it.getProductId(), it.getQuantity(), subtotal);
            if (!ok) {
                // If order_detail insertion fails, order will be incomplete
                // In production, use transaction to rollback
                System.err.println("Failed to insert order_detail for product " + it.getProductId());
            }
        }

        // Clear cart
        cartRepo.clearCart(customerId);
        return true;
    }
}
