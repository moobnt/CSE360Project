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

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
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
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	public void register(String username, String password, String email, String[] roles, boolean onetime, java.sql.Date date, String[] name) throws SQLException {
		if(isDatabaseEmpty()) roles = new String[] {"Admin"};
		
		String insertUser = "INSERT INTO cse360users "
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
	
	/**
	 * Updates a user represented by their username. Provide field in string format, and 
	 * provide the new value you want in that field.
	 * 
	 */
	public void update(String username, String field, Object value)  throws SQLException {
		String sql = "UPDATE cse360users SET " + field + " = ? WHERE username = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setObject(1, value);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		}
	}
	
	public void removeUser(String username) throws SQLException {
		String sql = "DELETE FROM cse360users WHERE username = ?";
		
		try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setObject(1, username);
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
	
	public boolean doesUserExist(String username) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, username);
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
	
	public void displayUsersByUser() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
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
	
	public void clearDatabase() throws SQLException {
		String sql = "TRUNCATE TABLE cse360users RESTART IDENTITY";
		Statement stmt = connection.createStatement();
		stmt.execute(sql);
	}
	
	public void dropTable() throws SQLException {
		String sql = "DROP TABLE cse360users";
		Statement stmt = connection.createStatement();
		stmt.execute(sql);
	}


	public void closeConnection() {
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
