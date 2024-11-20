package project.account;

import java.sql.*;
import java.time.OffsetDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;

public class DatabaseHelper {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/database";

    private static final String USER = "sa";
    private static final String PASS = "";

    private static Connection connection = null;
    private static Statement statement = null;
    private static PreparedStatement pstmt = null;

    /**
     * Establishes a connection to the database and creates necessary tables.
     * 
     * @return the database connection
     * @throws SQLException if there is an issue connecting to the database or creating tables
     */
    public static Connection connectToDatabase() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                statement = connection.createStatement();
                createTables();
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC Driver not found: " + e.getMessage());
            }
        }
        return connection;
    }
    
    /**
     * Closes the database connection and statement.
     */
    public static void closeConnection() throws SQLException{
            if (pstmt != null) pstmt.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
    }

    /**
     * Creates necessary tables in the database if they do not exist.
     */
    public static void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255), "
                + "roles VARCHAR(255), "
                + "onetime BOOLEAN DEFAULT FALSE, "
                + "onetimeCode VARCHAR(255), "        // New column for one-time code
                + "onetimeDate TIMESTAMP, "           // New column for one-time code expiration
                + "isReset BOOLEAN DEFAULT FALSE, "   // New column to track reset status
                + "fullName VARCHAR(255))";
        statement.execute(userTable);

        String codesTable = "CREATE TABLE IF NOT EXISTS codes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "code VARCHAR(255) UNIQUE NOT NULL, "
                + "roles VARCHAR(255))";
        statement.execute(codesTable);
    }
    
    public static void storeOneTimeCode(String username, String resetCode, OffsetDateTime expirationDate) throws SQLException {
        String sql = "UPDATE users SET onetimeCode = ?, onetimeDate = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, resetCode);
            pstmt.setTimestamp(2, Timestamp.from(expirationDate.toInstant()));
            pstmt.setString(3, username);
            pstmt.executeUpdate();
        }
    }

    public static boolean validateOneTimeCode(String username, String code) throws SQLException {
        String sql = "SELECT onetimeCode, onetimeDate FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedCode = rs.getString("onetimeCode");
                    Timestamp expirationTimestamp = rs.getTimestamp("onetimeDate");
                    OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

                    return storedCode != null && storedCode.equals(code) &&
                            expirationTimestamp != null &&
                            expirationTimestamp.toInstant().isAfter(currentDateTime.toInstant());
                }
            }
        }
        return false;
    }

    public static void clearOneTimeCode(String username) throws SQLException {
        String sql = "UPDATE users SET onetimeCode = NULL, onetimeDate = NULL WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }

    public static void updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ?, onetime = FALSE WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Adds a new user with specified roles.
     *
     * @param username the username of the user
     * @param roles    an array of roles assigned to the user
     */
    public static void addUser(String username, String[] roles) throws SQLException {
        String insertUserSQL = "INSERT INTO users (username, roles) VALUES (?, ?)";
        String rolesString = String.join(",", roles); // Convert roles array to a comma-separated string

        try (PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, rolesString);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Registers a new user in the 'users' table.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param email    the email of the user
     * @param roles    an array of roles assigned to the user
     * @param onetime  flag indicating whether the password is one-time
     * @param date     expiration date for the one-time password
     * @param name     full name of the user as an array with first, middle, and last names
     */
    public static void register(String username, String password, String email, Object[] roles,
            boolean onetime, OffsetDateTime date, String[] name) throws SQLException {
    	
    	if (date == null) {
    		date = OffsetDateTime.now(ZoneOffset.UTC); // Set to current time if null
    	}

    	String insertUserSQL = "INSERT INTO users (username, password, email, roles, onetime, onetimeDate, fullName) VALUES (?, ?, ?, ?, ?, ?, ?)";
    	String fullName = String.join(" ", name);  // Concatenate full name from the array

    	try (PreparedStatement pstmt = connection.prepareStatement(insertUserSQL)) {
    		pstmt.setString(1, username);
    		pstmt.setString(2, password);
    		pstmt.setString(3, email);
    		pstmt.setString(4, String.join(",", (CharSequence[]) roles));  // Store roles as a comma-separated string
    		pstmt.setBoolean(5, onetime);
    		pstmt.setTimestamp(6, Timestamp.from(date.toInstant()));
    		pstmt.setString(7, fullName);
    		pstmt.executeUpdate();
    	}
    }


    /**
     * Registers a new invitation code in the 'codes' table.
     */
    public static void registerCode(String code, String[] roles) throws SQLException {
        String sql = "INSERT INTO codes (code, roles) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setObject(2, roles, JDBCType.ARRAY);
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates a specified field in a table based on a condition.
     */
    public static void update(String table, String field, String key, String value, Object newValue) throws SQLException {
        String sql = "UPDATE " + table + " SET " + field + " = ? WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, newValue);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a specific value from a table based on a given condition.
     */
    public static Object getValue(String table, String key, String value, String field) throws SQLException {
        String sql = "SELECT " + field + " FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(field);
                }
            }
        }
        return null;
    }

    /**
     * Removes a record from the specified table based on a condition.
     */
    public static void remove(String table, String key, String value) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.executeUpdate();
        }
    }

    /**
     * Displays user information for admin purposes.
     */
    public static void displayUsersbyAdmin() throws SQLException {
        String sql = "SELECT username, roles, fullName FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Roles: " + rs.getString("roles"));
                System.out.println("Full Name: " + rs.getString("fullName"));
                System.out.println("---------------------------");
            }
        }
    }

    /**
     * Drops the specified table from the database.
     */
    public static void dropTable(String table) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + table;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table " + table + " has been dropped.");
        }
    }

    /**
     * Checks if a record exists in a specified table based on a given condition.
     */
    public static boolean doesExist(String table, String item, Object value) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + item + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
