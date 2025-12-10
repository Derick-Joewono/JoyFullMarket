package repository;

import helper.DatabaseConnection;
import model.Admin;
import java.sql.*;

public class AdminRepository {

    public Admin findByEmail(String email) {
        String query = "SELECT * FROM admin WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("adminId"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
