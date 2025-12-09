package repository;

import helper.DatabaseConnection;

import java.sql.*;

public class OrderRepository {

    public boolean assignCourier(int orderId, int courierId) {
        String q = "UPDATE orders SET courierId = ?, status = 'In Progress' WHERE orderId = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(q)) {

            ps.setInt(1, courierId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
