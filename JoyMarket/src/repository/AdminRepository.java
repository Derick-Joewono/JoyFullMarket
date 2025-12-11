package repository;

import helper.DatabaseConnection;
import model.Admin;
import java.sql.*;

public class AdminRepository {

    public Admin getAdminByEmail(String email) {
        String query = "SELECT * FROM admins WHERE admin_email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getInt("admin_id"),
                    rs.getString("admin_name"),
                    rs.getString("admin_email"),
                    rs.getString("admin_password")
                );
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
