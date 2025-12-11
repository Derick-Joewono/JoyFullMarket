package repository;

import helper.DatabaseConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private Connection conn;

    public ProductRepository() {
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

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("stock") >= qty;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean reduceStock(int productId, int qty) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean updateStock(int productId, int newStock) {
        String query = "UPDATE products SET stock = ? WHERE id = ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePrice(int productId, double newPrice) {
        String query = "UPDATE products SET price = ? WHERE id = ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(int productId, int newStock, double newPrice) {
        String query = "UPDATE products SET stock = ?, price = ? WHERE id = ?";
        Connection connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newStock);
            ps.setDouble(2, newPrice);
            ps.setInt(3, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertProduct(String name, double price, int stock) {
        String query = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("Database connection is null!");
            return false;
        }
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, stock);
            int result = ps.executeUpdate();
            System.out.println("Insert product result: " + result);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error inserting product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
