package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import helper.DatabaseConnection;
import model.Customer;

public class CustomerRepository {

    private final Connection conn;

    public CustomerRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO customers(full_name, email, password, phone, address, gender) VALUES(?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFull_name());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPassword());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setString(6, customer.getGender());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer(
                		rs.getInt("id"), 
                		rs.getString("full_name"),
                		rs.getString("email"),
                		rs.getString("password"),
                		rs.getString("phone"),
                		rs.getString("address"),
                		rs.getString("gender"));
                
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

   
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Customer c = new Customer(
                		rs.getInt("id"), 
                		rs.getString("full_name"),
                		rs.getString("email"),
                		rs.getString("password"),
                		rs.getString("phone"),
                		rs.getString("address"),
                		rs.getString("gender"));
                return c;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET full_name=?, email=?, password=?, phone=?, address=?, gender=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFull_name());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPassword());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setString(6, customer.getGender());
            ps.setInt(7, customer.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("id"), 
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("gender"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
