package helper;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

	public static DatabaseConnection instance;
	private Connection connection;
	private static final String URL = "jdbc:mysql://localhost:3306/joymarket";
	private static final String USER = "root";
	private static final String PASSWORD ="";
	
	private DatabaseConnection() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database Connected!");
		} catch (Exception e) {
			e.printStackTrace();
			  System.out.println("❌ Database Connection Failed!");
		}
	}
	public static DatabaseConnection getInstance() {
		if (instance ==null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
	
	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("✅ Database Reconnected!");
			}
		} catch (Exception e) {
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("✅ Database Reconnected after error!");
			} catch (Exception ex) {
				System.err.println("❌ Failed to reconnect database!");
				ex.printStackTrace();
			}
		}
		return connection;
	}
	
}
