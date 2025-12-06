package test;

import helper.DatabaseConnection;

public class TestDB {

	public static void main(String[] args) {
		DatabaseConnection.getInstance().getConnection();
	}
}
