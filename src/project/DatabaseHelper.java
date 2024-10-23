package project;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

class DatabaseHelper {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/database";

    // Database credentials (these should ideally be stored securely, e.g., using environment variables)
    static final String USER = "sa";
    static final String PASS = "";

    private static Connection connection = null;
    private static Statement statement = null;

    /**
     * Establishes a connection to the database and creates necessary tables.
     * 
     * @throws SQLException if there is an issue connecting to the database or creating tables
     */
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

    /**
     * Creates the 'users' and 'codes' tables if they do not exist.
     * 
     * @throws SQLException if there is an issue executing the SQL statements
     */
    public static void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) NOT NULL, "
                + "roles VARCHAR(255), "
                + "onetime BOOLEAN DEFAULT FALSE, "
                + "onetimeDate DATE, "
                + "fullName VARCHAR(255)"
                + ")";

        statement.execute(userTable);

        String codesTable = "CREATE TABLE IF NOT EXISTS codes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "code VARCHAR(255) UNIQUE NOT NULL, "
                + "roles VARCHAR(255)"
                + ")";

        statement.execute(codesTable);
    }

    /**
     * Displays all user records in the 'users' table.
     * 
     * @throws SQLException if there is an issue executing the query
     */
    public static void displayUsersByUser() throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean usersExist = false;
            while (rs.next()) {
                usersExist = true;
                // Retrieve data by column name
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String roles = rs.getString("roles");
                Boolean onetime = rs.getBoolean("onetime");
                Date onetimeDate = rs.getDate("onetimeDate");
                String fullName = rs.getString("fullName");

                // Display values
                System.out.println("\nID: " + id);
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                System.out.println("Email: " + email);
                System.out.println("Roles: " + roles);
                System.out.println("One-Time Password Enabled: " + onetime);
                if (onetimeDate != null) {
                    System.out.println("One-Time Expiration Date: " + onetimeDate.toString());
                } else {
                    System.out.println("One-Time Expiration Date: Not Set");
                }
                System.out.println("Full Name: " + fullName);
            }

            if (!usersExist) {
                System.out.println("\nNo Users Registered in the Database!");
            }
        }
    }

    /**
     * Clears all records from the specified table and resets auto-incrementing IDs.
     * 
     * @throws SQLException if there is an issue executing the truncate statement
     */
    public static void clearDatabase() throws SQLException {
        String sql = "TRUNCATE TABLE users RESTART IDENTITY";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database has been cleared.");
        }
    }

    /**
     * Drops the specified table from the database.
     * 
     * @param table the name of the table to drop
     * @throws SQLException if there is an issue executing the drop statement
     */
    public static void dropTable(String table) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + table;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table " + table + " has been dropped.");
        }
    }

    /**
     * Checks if the database is empty.
     * 
     * @return true if the 'users' table has no records, false otherwise
     * @throws SQLException if there is an issue executing the query
     */
    public static boolean isDatabaseEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM users";
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("total") == 0;
            }
        }
        return true;
    }

    /**
     * Registers a new user in the 'users' table.
     * 
     * @param username the username of the new user
     * @param password the password for the new user
     * @param email    the email address of the new user
     * @param roles    the roles assigned to the new user
     * @param onetime  indicates if a one-time password is enabled
     * @param date     the expiration date for the one-time password
     * @param name     the full name of the user
     * @throws SQLException if there is an issue executing the SQL insert
     */
    public static void register(String username, String password, String email, Object[] roles, boolean onetime, OffsetDateTime date, String[] name) throws SQLException {
        if (isDatabaseEmpty()) {
            roles = new String[] {"Admin"}; // First user is assigned the Admin role
        }
        if (doesExist("users", "username", username)) {
            return; // User already exists, do not proceed
        }

        String insertUser = "INSERT INTO users (username, password, email, roles, onetime, onetimeDate, fullName) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
        	pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			pstmt.setObject(4, roles, JDBCType.ARRAY);
			pstmt.setBoolean(5, onetime);
			pstmt.setObject(6, date);
			pstmt.setObject(7, name);
			pstmt.executeUpdate();
        }
    }

    /**
     * Registers a new invitation code in the 'codes' table.
     * 
     * @param code  the invitation code
     * @param roles the roles associated with the code
     * @throws SQLException if there is an issue executing the SQL insert
     */
    public static void registerCode(String code, String[] roles) throws SQLException {
        if (doesExist("codes", "code", code)) {
            return; // Code already exists, do not proceed
        }

        String insertCode = "INSERT INTO codes (code, roles) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertCode)) {
            pstmt.setString(1, code);
            pstmt.setObject(2, roles, JDBCType.ARRAY);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a specific value from a table based on a given condition.
     * 
     * @param table the table to query
     * @param key   the column name used for the condition
     * @param value the value to match in the key column
     * @param field the field to retrieve
     * @return the value from the specified field if found, null otherwise
     * @throws SQLException if there is an issue executing the query
     */
    public static Object getValue(String table, String key, Object value, String field) throws SQLException {
        String sql = "SELECT " + field + " FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(field);
                }
            }
        }
        return null;
    }

    /**
     * Updates a specified field in a table based on a condition.
     * 
     * @param table    the table to update
     * @param field    the field to update
     * @param key      the column used for the condition
     * @param value    the value to match in the key column
     * @param newValue the new value to set in the field
     * @throws SQLException if there is an issue executing the update
     */
    public static void update(String table, String field, String key, Object value, Object newValue) throws SQLException {
        String sql = "UPDATE " + table + " SET " + field + " = ? WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, newValue);
            pstmt.setObject(2, value);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a record from a specified table.
     * 
     * @param table the table to delete from
     * @param key   the column used for the condition
     * @param value the value to match in the key column
     * @throws SQLException if there is an issue executing the delete
     */
    public static void remove(String table, String key, String value) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, value);
            pstmt.executeUpdate();
        }
    }

    /**
     * Checks if a record exists in a specified table based on a given condition.
     * 
     * @param table the table to query
     * @param item  the column name used for the condition
     * @param value the value to match in the item column
     * @return true if the record exists, false otherwise
     */
    public static boolean doesExist(String table, String item, Object value) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + item + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void resetUser(String username) {
		// there are a few things that have to be set, so this function is called for each
		// This figures out the time in UTC that the reset function is called
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		
		// TODO: update all fields individually and figure out update
	}

    /**
     * Closes the database connection and statement.
     */
    public static void closeConnection() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
