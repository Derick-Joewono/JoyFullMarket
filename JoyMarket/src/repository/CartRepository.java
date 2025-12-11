package repository;

import helper.DatabaseConnection;
import model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private Connection conn;

    public CartRepository() {
        getConnection();
    }

    private Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DatabaseConnection.getInstance().getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conn = DatabaseConnection.getInstance().getConnection();
        }
        return conn;
    }

    // Add new or increase quantity if product already in cart
    public boolean addOrUpdateCart(int customerId, int productId, int qty) {
        String checkSql = "SELECT id, quantity FROM cart WHERE customer_id = ? AND product_id = ?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(checkSql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int existing = rs.getInt("quantity");
                String update = "UPDATE cart SET quantity = ? WHERE id = ?";
                try (PreparedStatement up = connection.prepareStatement(update)) {
                    up.setInt(1, existing + qty);
                    up.setInt(2, id);
                    return up.executeUpdate() > 0;
                }
            } else {
                String insert = "INSERT INTO cart(customer_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insert)) {
                    ins.setInt(1, customerId);
                    ins.setInt(2, productId);
                    ins.setInt(3, qty);
                    return ins.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update quantity by cart id
    public boolean updateQuantity(int cartId, int qty) {
        if (qty <= 0) {
            return deleteCartItem(cartId);
        }
        String sql = "UPDATE cart SET quantity = ? WHERE id = ?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Delete item by cart id
    public boolean deleteCartItem(int cartId) {
        String sql = "DELETE FROM cart WHERE id = ?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Clear cart for customer
    public boolean clearCart(int customerId) {
        String sql = "DELETE FROM cart WHERE customer_id = ?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Helper: get productId by cartId (needed in controller validation)
    public Integer getProductIdByCartId(int cartId) {
        String sql = "SELECT product_id FROM cart WHERE id = ?";
        Connection connection = getConnection();
        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("product_id");
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Return list of CartItem (JOIN cart + products to get productName & price)
    public List<CartItem> getCartItemsByCustomer(int customerId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id as cart_id, p.id as product_id, p.name, p.price, c.quantity " +
                     "FROM cart c JOIN products p ON c.product_id = p.id WHERE c.customer_id = ?";
        Connection connection = getConnection();
        if (connection == null) return items;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new CartItem(
                    rs.getInt("cart_id"),
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }
}
