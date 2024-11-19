package project.group;

import java.sql.*;
import java.util.*;

import project.account.DatabaseModel;

/**
 * <p> GroupDatabase Class </p>
 * 
 * <p> Description: Class to manage database operations for groups </p>
 * 
 * @version 1.00 2024-11-13 Initial baseline
 */
public class SpecialGroupDatabase extends DatabaseModel {
    Statement stmt;
    private final String TABLE_NAME = "special_group";
    private final String DEFAULT_GROUP_NAME = "unprotected";
    private final String ARRAY_SEPERATOR = ", "; // item seperator in the SQL tables

    /**
     * Default constructor; connects to the databse and creates tables
     */
    public SpecialGroupDatabase() {
        connect();

        try {
            createTables();
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
    
    /**
     * Sets up the table initially and makes it up-to-date
     * 
     * @throws SQLException
     */
    private void createTables() throws SQLException {
        stmt = connection.createStatement();

        String groupTable = "CREATE TABLE IF NOT EXISTS ? ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "title VARCHAR(255) UNIQUE NOT NULL, "
            + "admin VARCHAR(255), "
            + "instructors VARCHAR(255), "
            + "students VARCHAR(65535), "
            + "articles VARCHAR(255))";

        try (PreparedStatement pstmt = connection.prepareStatement(groupTable)) {
            pstmt.setString(1, TABLE_NAME);

            pstmt.executeUpdate();
        }

        // creates a default group that will not encrypt any articles
        // anyone can access this group, but because it is so simple
        // it does not have an admin
        //
        // Every instructor and student should be in this group
        String defaultGroup = "INSERT INTO ? (title, admin, instructors, students, articles)"
                + "VALUES (?, NULL, NULL, NULL, NULL)"
                + "WHERE NOT EXISTS (SELECT 1 FROM ? WHERE title = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(defaultGroup)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, DEFAULT_GROUP_NAME);
            pstmt.setString(3, TABLE_NAME);
            pstmt.setString(4, DEFAULT_GROUP_NAME);

            pstmt.executeUpdate();
        }

        // finds all instructors / students in the database
        String findAll = "SELECT username FROM users WHERE CONTAINS (roles, ?)";

        String allInstructors = null;
        String allStudents = null;

        try (PreparedStatement pstmt = connection.prepareStatement(findAll)) {
            pstmt.setString(1, "Instructor");

            ResultSet instructorUsernames = pstmt.executeQuery();

            if (instructorUsernames != null) {
                do {
                    allInstructors = addItemToString(allInstructors, 
                                        instructorUsernames.getString("username"));
                } while (instructorUsernames.next());
            } else {
                System.err.println("No instructors found");
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(findAll)) {
            pstmt.setString(1, "Student");

            ResultSet studentUsernames = pstmt.executeQuery();

            if (studentUsernames != null) {
                do {
                    allStudents = addItemToString(allStudents, 
                                    studentUsernames.getString("username"));
                } while (studentUsernames.next());
            } else {
                System.err.println("No students found");
            }
        }

        // adds every currently existing person to the group
        // if they aren't already in it
        String addAll = "UPDATE ? SET instructors = ?, students = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addAll)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, allInstructors);
            pstmt.setString(3, allStudents);
            pstmt.setString(4, DEFAULT_GROUP_NAME);

            pstmt.executeUpdate();
        }
    }

    /**
     * Method that creates a new group
     * Should only be called by admin
     * 
     * @param title Unique title of the group
     * @param admin Instructor(s) to make admin
     * @param instructors Instructors with access to the group
     * @param students Students with access to the group
     * @param articles Articles to add to the group by ID
     */
    public void createGroup(String title, String admin, String instructors, String students, String articles) {
        String createGroup = "INSERT INTO ? (title, admin, instructors, students, articles) "
                + "VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement pstmt = connection.prepareStatement(createGroup)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, title);
            pstmt.setString(3, admin);
            pstmt.setString(4, instructors);
            pstmt.setString(5, students);
            pstmt.setString(6, articles);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Table already exists");
        }
    }

    /**
     * Method that deletes a group
     * Should only be called by admin
     * 
     * @param title Unique title of the group
     */
    public void deleteGroup(String title) {
        String deleteGroup = "DELETE FROM ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteGroup)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, title);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds a person to a special group
     * 
     * @param title The title of the group
     * @param role The role of the person to be added
     * @param person The username of the person to be added
     */
    public void addPerson(String title, String role, String person) {
        String roleListString = null;
        String getPeople = "SELECT ? FROM ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getPeople)) {
            pstmt.setString(1, role);
            pstmt.setString(2, TABLE_NAME);
            pstmt.setString(3, title);

            ResultSet resultSet = pstmt.executeQuery();

            roleListString = addItemToString(resultSet.getString(role), person);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String addPerson = "UPDATE ? SET ? = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addPerson)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, roleListString);
            pstmt.setString(3, title);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a person from a specific group
     * 
     * @param title Unique title of the group
     * @param role The role of the person to be removed
     * @param person Person to remove from the group
     */
    public void removePerson(String title, String role, String person) {
        String roleListString = null;
        String getPeople = "SELECT ? FROM ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getPeople)) {
            pstmt.setString(1, role);
            pstmt.setString(2, TABLE_NAME);
            pstmt.setString(3, title);

            ResultSet studentResultSet = pstmt.executeQuery();

            roleListString = removeItemFromString(studentResultSet.getString("students"), person);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String removeStudent = "UPDATE ? SET students = ? WHERE title = ?"; 

        try (PreparedStatement pstmt = connection.prepareStatement(removeStudent)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, roleListString);
            pstmt.setString(3, title); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Converts a string into a list, adds an item, and changes it back to a string
     * 
     * @param initString The initial string that is passed in
     * @param item The item to be added
     * @return String with item added to the end
     */
    private String addItemToString(String initString, String item) {
        List<String> itemArray = new ArrayList<>();

        itemArray = Arrays.asList(initString.split(ARRAY_SEPERATOR));
        itemArray.add(item);

        return String.join(ARRAY_SEPERATOR, itemArray);
    }

    /**
     * Converts a string into a list, removes the item, and changes it back to a string
     * 
     * @param initString The initial string that is passed in
     * @param item The item to be removed
     * @return String with item removed
     */
    private String removeItemFromString(String initString, String item) {
        List<String> itemArray = new ArrayList<>();

        itemArray = Arrays.asList(initString.split(ARRAY_SEPERATOR));
        itemArray.remove(item);

        return String.join(ARRAY_SEPERATOR, itemArray);
    }
}
