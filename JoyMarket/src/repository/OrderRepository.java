package repository;

import helper.DatabaseConnection;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final Connection conn;

    public OrderRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    public boolean assignCourier(int orderId, int courierId) {
        String q = "UPDATE orders SET courier_id = ?, delivery_status = 'IN_PROGRESS' WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, courierId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByCourier(int courierId) {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE courier_id = ? ORDER BY id DESC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, courierId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer courierIdVal = rs.getObject("courier_id") != null ? rs.getInt("courier_id") : null;
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getDouble("total"),
                    rs.getString("status"),
                    courierIdVal,
                    rs.getString("delivery_status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY id DESC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Integer courierId = rs.getObject("courier_id") != null ? rs.getInt("courier_id") : null;
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getDouble("total"),
                    rs.getString("status"),
                    courierId,
                    rs.getString("delivery_status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Integer courierId = rs.getObject("courier_id") != null ? rs.getInt("courier_id") : null;
                return new Order(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getDouble("total"),
                    rs.getString("status"),
                    courierId,
                    rs.getString("delivery_status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateDeliveryStatus(int orderId, String deliveryStatus) {
        String query = "UPDATE orders SET delivery_status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, deliveryStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertOrder(Order order) {
        String sql = "INSERT INTO orders(customer_id, total, status, courier_id, delivery_status) VALUES(?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getCustomerId());
            ps.setDouble(2, order.getTotal());
            ps.setString(3, order.getStatus());
            if (order.getCourierId() != null) {
                ps.setInt(4, order.getCourierId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, order.getDeliveryStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
