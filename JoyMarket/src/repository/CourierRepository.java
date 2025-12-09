package repository;

import helper.DatabaseConnection;
import model.Courier;

import java.sql.*;
import java.util.ArrayList;

public class CourierRepository {

    public ArrayList<Courier> getAllCouriers() {
        ArrayList<Courier> list = new ArrayList<>();
        String query = "SELECT * FROM courier";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Courier(
                    rs.getInt("courierId"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
