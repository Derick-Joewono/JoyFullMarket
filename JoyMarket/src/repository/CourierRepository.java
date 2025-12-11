package repository;

import helper.DatabaseConnection;
import model.Courier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierRepository {

    private Connection conn;

    public CourierRepository() {
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

    public List<Courier> getAllCouriers() {
        List<Courier> list = new ArrayList<>();
        String query = "SELECT * FROM couriers WHERE courier_status = 'ACTIVE'";
        Connection connection = getConnection();
        if (connection == null) return list;

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Courier(
                    rs.getInt("courier_id"),
                    rs.getString("courier_name"),
                    rs.getString("courier_email"),
                    rs.getString("courier_phone"),
                    rs.getString("courier_password"),
                    rs.getString("courier_status"),
                    rs.getString("vehicle_type"),
                    rs.getString("vehicle_plate")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Courier getCourierById(int courierId) {
        String query = "SELECT * FROM couriers WHERE courier_id = ?";
        Connection connection = getConnection();
        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, courierId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Courier(
                    rs.getInt("courier_id"),
                    rs.getString("courier_name"),
                    rs.getString("courier_email"),
                    rs.getString("courier_phone"),
                    rs.getString("courier_password"),
                    rs.getString("courier_status"),
                    rs.getString("vehicle_type"),
                    rs.getString("vehicle_plate")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Courier getCourierByEmail(String email) {
        String query = "SELECT * FROM couriers WHERE courier_email = ?";
        Connection connection = getConnection();
        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Courier(
                    rs.getInt("courier_id"),
                    rs.getString("courier_name"),
                    rs.getString("courier_email"),
                    rs.getString("courier_phone"),
                    rs.getString("courier_password"),
                    rs.getString("courier_status"),
                    rs.getString("vehicle_type"),
                    rs.getString("vehicle_plate")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Courier getCourierByPhone(String phone) {
        String query = "SELECT * FROM couriers WHERE courier_phone = ?";
        Connection connection = getConnection();
        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Courier(
                    rs.getInt("courier_id"),
                    rs.getString("courier_name"),
                    rs.getString("courier_email"),
                    rs.getString("courier_phone"),
                    rs.getString("courier_password"),
                    rs.getString("courier_status"),
                    rs.getString("vehicle_type"),
                    rs.getString("vehicle_plate")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCourier(Courier courier) {
        String sql = "INSERT INTO couriers(courier_name, courier_email, courier_phone, courier_password, courier_status, vehicle_type, vehicle_plate) VALUES(?,?,?,?,?,?,?)";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courier.getName());
            ps.setString(2, courier.getEmail());
            ps.setString(3, courier.getPhone());
            ps.setString(4, courier.getPassword());
            ps.setString(5, courier.getStatus());
            ps.setString(6, courier.getVehicleType());
            ps.setString(7, courier.getVehiclePlate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCourier(Courier courier) {
        String sql = "UPDATE couriers SET courier_name=?, courier_email=?, courier_phone=?, courier_password=?, courier_status=?, vehicle_type=?, vehicle_plate=? WHERE courier_id=?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courier.getName());
            ps.setString(2, courier.getEmail());
            ps.setString(3, courier.getPhone());
            ps.setString(4, courier.getPassword());
            ps.setString(5, courier.getStatus());
            ps.setString(6, courier.getVehicleType());
            ps.setString(7, courier.getVehiclePlate());
            ps.setInt(8, courier.getCourierId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateVehicleInfo(int courierId, String vehicleType, String vehiclePlate) {
        String sql = "UPDATE couriers SET vehicle_type=?, vehicle_plate=? WHERE courier_id=?";
        Connection connection = getConnection();
        if (connection == null) return false;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vehicleType);
            ps.setString(2, vehiclePlate);
            ps.setInt(3, courierId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
