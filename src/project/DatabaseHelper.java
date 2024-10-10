package project;

import java.sql.*;
import java.sql.SQLType;
import java.util.Map;
import java.util.Date;


class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/database";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private static Connection connection = null;
	private static Statement statement = null; 
	//	PreparedStatement pstmt

	public static void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
	public static void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(255) UNIQUE,"
				+ "password VARCHAR(255), "
				+ "email VARCHAR(255) UNIQUE, "
				+ "roles VARCHAR(255) ARRAY,"
				+ "onetime BOOLEAN,"
				+ "onetimeDate DATE,"
				+ "fullName VARCHAR(255) ARRAY"
				+ ")";
		
		statement.execute(userTable);
		
		String codesTable = "CREATE TABLE IF NOT EXISTS codes ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "code VARCHAR(255) UNIQUE, "
				+ "roles VARCHAR(255) ARRAY"
				+ ")";
		
		statement.execute(codesTable);
	}


	// Check if the database is empty
	public static boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT"
				+ "(SELECT COUNT(*) FROM users) AS count,"
				+ "(SELECT COUNT(*) FROM codes) AS count2,"
				+ "FROM dual";
		ResultSet resultSet = statement.executeQuery(query);

		if (resultSet.next()) {
			return resultSet.getInt("count") + resultSet.getInt("count2") == 0;
		}
		return true;
	}

	public static void register(String username, String password, String email, String[] roles, boolean onetime, java.sql.Date date, String[] name) throws SQLException {
		if(isDatabaseEmpty()) roles = new String[] {"Admin"};
		if(doesExist("users", "username", username)) return;
		
		String insertUser = "INSERT INTO users "
				+ "(username, password, email, roles, onetime, onetimeDate, fullName) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			pstmt.setObject(4, roles, JDBCType.ARRAY);
			pstmt.setBoolean(5, onetime);
			pstmt.setDate(6, date);
			pstmt.setObject(7, name);
			pstmt.executeUpdate();
		}
	}
	
	public static void register(String code, String[] roles) throws SQLException {
		if(doesExist("codes", "code", code)) return;
		
		String insertCode = "INSERT INTO codes "
				+ "(code, roles) "
				+ "VALUES (?,?)";
		
		try (PreparedStatement pstmt = connection.prepareStatement(insertCode)) {
			pstmt.setString(1, code);
			pstmt.setObject(2, roles, JDBCType.ARRAY);

			pstmt.executeUpdate();
		}
	}
	
	public static Object getValue(String table, String key, Object value, String field) throws SQLException {
		String sql = "SELECT " + field + " FROM " + table + " WHERE " + key + " = '" + value + "'";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			return rs.getObject(field);
		};
		
		return null;
	}
	
	/**
	 * Updates a user represented by their username. Provide field in string format, and 
	 * provide the new value you want in that field.
	 * 
	 */
	public static void update(String table, String field, String key, Object value, Object newValue)  throws SQLException {
		String sql = "UPDATE " + table + " SET " + field + " = ? WHERE " + key + " = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setObject(1, newValue);
			pstmt.setObject(2, value);
			pstmt.executeUpdate();
		}
	}
	
	public static void remove(String table, String key, String value) throws SQLException {
		String sql = "DELETE FROM " + table + " WHERE " + key + " = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setObject(1, value);
			pstmt.executeUpdate();
		}
	}

//	public boolean login(String email, String password, String role) throws SQLException {
//		String query = "SELECT * FROM cse360users WHERE email = ? AND password = ? AND role = ?";
//		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
//			pstmt.setString(1, email);
//			pstmt.setString(2, password);
//			pstmt.setString(3, role);
//			try (ResultSet rs = pstmt.executeQuery()) {
//				return rs.next();
//			}
//		}
//	}
	
	public static boolean doesExist(String table, String item, Object value) {
	    String query = "SELECT COUNT(*) FROM " + table + " WHERE " + item + " = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setObject(1, value);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

//	public void displayUsersByAdmin() throws SQLException{
//		String sql = "SELECT * FROM cse360users"; 
//		Statement stmt = connection.createStatement();
//		ResultSet rs = stmt.executeQuery(sql); 
//
//		while(rs.next()) { 
//			// Retrieve by column name 
//			int id  = rs.getInt("id"); 
//			String  email = rs.getString("email"); 
//			String username = rs.getString("username");
//			String password = rs.getString("password"); 
//			Array role = rs.getArray("role");  
//
//			// Display values 
//			System.out.print("ID: " + id); 
//			System.out.print(", Username: " + username);
//			System.out.print(", Password: " + password); 
//			System.out.print(", Email: " + email); 
//			System.out.println(", Roles: " + role); 
//		} 
//	}
	
	public static void displayUsersByUser() throws SQLException{
		String sql = "SELECT * FROM users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		boolean users = false;
		while(rs.next()) {
			users = true;
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String username = rs.getString("username");
			String password = rs.getString("password"); 
			Object[] roles = (Object[]) rs.getArray("roles").getArray(); 
			Boolean onetime = rs.getBoolean("onetime");
			Date date = new Date();
			date.setDate(rs.getDate("oneTimeDate").getDate());
			Object[] name = (Object[]) rs.getArray("fullname").getArray(); 

			
			// Display values 
			System.out.println("\nID: " + id); 
			System.out.println("Username: " + username);
			System.out.println("Password: " + password); 
			System.out.println("Email: " + email); 
			System.out.println("Roles: " + roles[0]); 
			System.out.println("One Time: " + onetime);
			System.out.println("One Time Exiration Date: " + date.toLocaleString());
			System.out.println("Name: " + name[0]);
		} 
		
		if(!users) System.out.println("\nNo Users Registered in the Database!");
	}
	
	public static void clearDatabase() throws SQLException {
		String sql = "TRUNCATE TABLE cse360users RESTART IDENTITY";
		Statement stmt = connection.createStatement();
		stmt.execute(sql);
	}
	
	public static void dropTable(String table) throws SQLException {
		String sql = "DROP TABLE " + table;
		Statement stmt = connection.createStatement();
		stmt.execute(sql);
	}


	public static void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

}
