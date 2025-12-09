package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import helper.DatabaseConnection;

public class CustomerBalanceRepository {

    private final Connection conn;

    public CustomerBalanceRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ---------- GET BALANCE ----------
    public double getBalance(int customerId) {
        String sql = "SELECT balance FROM customer_balance WHERE customer_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                // Jika belum ada record, buat default balance 0
                createBalanceIfNotExists(customerId);
                return 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ---------- CREATE BALANCE IF NOT EXISTS ----------
    private void createBalanceIfNotExists(int customerId) {
        String checkSql = "SELECT COUNT(*) FROM customer_balance WHERE customer_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO customer_balance (customer_id, balance) VALUES (?, 0)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, customerId);
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- TOP UP ----------
    public boolean topUp(int customerId, double amount) {
        String sql = "UPDATE customer_balance SET balance = balance + ? WHERE customer_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    // ---------- SET BALANCE (Optional, force set) ----------
    public boolean setBalance(int customerId, double newBalance) {
        String sql = "UPDATE customer_balance SET balance = ? WHERE customer_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
