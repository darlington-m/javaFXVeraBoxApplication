package DataRetrival;

import java.sql.*;

public class MySQLConnect {
	// JDBC driver name and database URL
	@SuppressWarnings("unused")
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://helios.csesalford.com/agile15_group7";

	// Database credentials
	private static final String USER = "agile15";
	private static final String PASS = "vZjcDYl2oHFdlFrd";

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet resultSet = null;

	public MySQLConnect() {
	}

	public void insertRow(String sql) {

		try {
			System.out.println(sql);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			 // finally block used to close resources
			 try {
			 if (stmt != null)
			 conn.close();
			 } catch (SQLException se) {
			 }// do nothing
			 try {
			 if (conn != null)
			 conn.close();
			 } catch (SQLException se) {
			 se.printStackTrace();
			 }// end finally try
		}// end try
	}

	public ResultSet getRows(String sql) throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		resultSet = stmt.executeQuery(sql);
		return resultSet;
	}

}