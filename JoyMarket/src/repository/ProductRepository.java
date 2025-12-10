package repository;

import helper.DatabaseConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final Connection conn;

    public ProductRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean isStockAvailable(int productId, int qty) {
        String sql = "SELECT stock FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("stock") >= qty;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean reduceStock(int productId, int qty) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean updateStock(int productId, int newStock) {
        String query = "UPDATE product SET stock = ? WHERE productId = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newStock);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
