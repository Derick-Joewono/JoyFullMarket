package repository;

import helper.DatabaseConnection;
import model.Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductRepository {

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("productId"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStock(int productId, int newStock) {
        String query = "UPDATE product SET stock = ? WHERE productId = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newStock);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
