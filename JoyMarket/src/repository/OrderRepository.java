package repository;

import helper.DatabaseConnection;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private Connection conn;

    public OrderRepository() {
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

    public boolean assignCourier(int orderId, int courierId) {
        String q = "UPDATE orders SET courier_id = ?, delivery_status = 'PENDING' WHERE id = ?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(q)) {
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
        Connection connection = getConnection();
        if (connection == null) return list;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
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

    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ? ORDER BY id DESC";
        Connection connection = getConnection();
        if (connection == null) return list;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

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

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY id DESC";
        Connection connection = getConnection();
        if (connection == null) return list;

        try (Statement st = connection.createStatement();
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
        Connection connection = getConnection();
        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

    public int insertOrderAndGetId(Order order) {
        String sql = "INSERT INTO orders(customer_id, total, status, courier_id, delivery_status) VALUES(?,?,?,?,?)";
        Connection connection = getConnection();
        if (connection == null) return -1;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getCustomerId());
            ps.setDouble(2, order.getTotal());
            ps.setString(3, order.getStatus());
            if (order.getCourierId() != null) {
                ps.setInt(4, order.getCourierId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, order.getDeliveryStatus());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
