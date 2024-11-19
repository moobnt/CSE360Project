package project.account;

import java.sql.*;
import java.util.*;
import java.time.*;

public class DatabaseModel {
    protected Connection connection;

    /**
     * Establishes the connection to the database if needed
     */
    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseHelper.connectToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database if needed
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                DatabaseHelper.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the 'users' table in the database is empty.
     *
     * @return true if no users exist, false otherwise
     */
    public boolean isDatabaseEmpty() {
        boolean res = true;

        String query = "SELECT COUNT(*) FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                res = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Retrieves the roles associated with a given username.
     *
     * @param username the username of the user
     * @return an array of roles associated with the user
     */
    public String[] getUserRoles(String username) {
        String query = "SELECT roles FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String rolesString = rs.getString("roles");
                if (rolesString != null) {
                    return rolesString.split(","); // Split the roles string into an array
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0]; // Return an empty array if no roles are found or an error occurs
    }

    public void updateUserRoles(String username, String[] roles) {
        try {
            String rolesString = String.join(",", roles);
            DatabaseHelper.update("users", "roles", "username", username, rolesString);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    /**
     * Checks if a user's account has been reset and requires a one-time code for login.
     */
    public boolean isUserReset(String username) {
        String query = "SELECT isReset FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isReset"); // Check the isReset column
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the user does not exist or if there's an issue
    }

    /**
     * Validates if the provided one-time code is correct and has not expired.
     */
    public boolean validateOneTimeCode(String username, String code) {
        String query = "SELECT onetimeCode, onetimeDate FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedCode = rs.getString("onetimeCode");
                OffsetDateTime expirationDate = rs.getObject("onetimeDate", OffsetDateTime.class);
                
                if (storedCode != null && storedCode.equals(code) && expirationDate.isAfter(OffsetDateTime.now(ZoneOffset.UTC))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the user's password and clears the one-time flag.
     */
    public void updatePassword(String username, String newPassword) {
        String query = "UPDATE users SET password = ?, onetime = false WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the one-time code and resets the flag for the user.
     */
    public void clearOneTimeCode(String username) {
        String query = "UPDATE users SET onetimeCode = NULL, onetime = false WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a one-time code and expiration for the user account reset.
     */
    public void resetUser(String username, String code, OffsetDateTime expirationDate) {
        String query = "UPDATE users SET onetime = true, onetimeCode = ?, onetimeDate = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.setObject(2, expirationDate);
            stmt.setString(3, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetUserWithCode(String username, String resetCode, OffsetDateTime expirationDate) {
        if (doesUserExist(username)) {
            // Set the user's reset status and store the one-time code and expiration date
            editUser(username, "isReset", true);
            editUser(username, "onetimeCode", resetCode);
            editUser(username, "onetimeDate", Timestamp.from(expirationDate.toInstant()));
        } else {
            System.out.println("User does not exist");
        }
    }

    /**
     * Checks if a specific user has the Admin role.
     *
     * @param username the username to check
     * @return true if the user has the Admin role, false otherwise
     */
    public boolean isUserAdmin(String username) {
        String roles = (String) getUserField(username, "roles");
        return roles != null && roles.contains("Admin");
    }

    /**
     * Checks if a user with the specified username exists in the database.
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean doesUserExist(String username) {
        boolean res = DatabaseHelper.doesExist("users", "username", username);
        return res;
    }

    /**
     * Registers a new user only if the username is unique.
     */
    public void registerUser(String username, String password, String email, Object[] roles,
                             boolean onetime, OffsetDateTime date, String[] name) {
        if (doesUserExist(username)) {
            System.out.println("User with this username already exists. Choose a different username."); // TODO: Error message popup instead of println
            return;
        }
        try {
            DatabaseHelper.register(username, password, email, roles, onetime, date, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new invitation code with associated roles.
     */
    public void registerCode(String code, String... roles) {
        try {
            DatabaseHelper.registerCode(code, roles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new user with specified roles.
     */
    public void addUser(String username, String[] roles) {
        try {
            DatabaseHelper.addUser(username, roles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits a user's specified field with a new value.
     */
    public void editUser(String username, String field, Object newValue) {
        try {
            DatabaseHelper.update("users", field, "username", username, newValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasCode(String code) {
        return DatabaseHelper.doesExist("codes", "code", code);
    }

    /**
     * Returns the roles associated with a code as an array.
     */
    public String[] getCodeRoles(String code) {
        String rolesString = null;

        try {
            rolesString = (String) DatabaseHelper.getValue("codes", "code", code, "roles");
            if (rolesString != null) {
                System.out.println("Splitting roles string from database");
                return rolesString.split(",");
            } else {
                System.out.println("uh oh");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Resets a user's password and sets an expiration date for a one-time password.
     */
    public void resetUser(String username) {
        OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
        if (DatabaseHelper.doesExist("users", "username", username)) {
            editUser(username, "onetime", true);
            editUser(username, "password", "1234");
            editUser(username, "onetimeDate", currentTime.plusWeeks(1));
        } else {
            System.out.println("User does not exist");
        }
    }

    /**
     * Retrieves a user field value based on username.
     */
    public Object getUserField(String username, String field) {
        try {
            return DatabaseHelper.getValue("users", "username", username, field);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds additional roles to an existing code.
     */
    public void addCodeRoles(String code, String... roles) {
        Set<String> roleSet = new HashSet<>(List.of(getCodeRoles(code)));
        roleSet.addAll(List.of(roles));
        try {
            DatabaseHelper.update("codes", "roles", "code", code, String.join(",", roleSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a user from the database based on username.
     */
    public void removeUser(String username) {
        try {
            DatabaseHelper.remove("users", "username", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an invitation code from the database.
     */
    public void removeCode(String code) {
        try {
            DatabaseHelper.remove("codes", "code", code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays user data for admin use.
     */
    public List<String> displayUsersByAdmin() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String roles = rs.getString("roles");
                String fullName = rs.getString("fullName");

                String userInfo = "Username: " + username + ", Roles: " + roles + ", Full Name: " + fullName;
                users.add(userInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Drops the 'codes' table from the database.
     */
    public void dropCodesTable() {
        try {
            DatabaseHelper.dropTable("codes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drops the 'users' table from the database.
     */
    public void dropUserTable() {
        try {
            DatabaseHelper.dropTable("users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the 'users' table.
     */
    public void resetUserTable() {
        dropUserTable();
        try {
            DatabaseHelper.createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the 'codes' table.
     */
    public void resetCodeTable() {
        dropCodesTable();
        try {
            DatabaseHelper.createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets all tables in the database.
     */
    public void resetTables() {
        resetUserTable();
        resetCodeTable();
    }
}
