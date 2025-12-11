package repository;

import helper.DatabaseConnection;
import model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepository {

    private Connection conn;

    public OrderDetailRepository() {
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

    public boolean insertOrderDetail(int orderId, int productId, int quantity, double subtotal) {
        String sql = "INSERT INTO order_detail (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, subtotal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting order detail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.*, p.name as product_name FROM order_detail od " +
                     "JOIN products p ON od.product_id = p.id " +
                     "WHERE od.order_id = ?";
        Connection connection = getConnection();
        if (connection == null) return list;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                    rs.getInt("id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("subtotal")
                );
                detail.setProductName(rs.getString("product_name"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

