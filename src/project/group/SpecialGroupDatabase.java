package project.group;

import java.sql.*;
import java.util.*;

import project.DatabaseModel;

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
    private final String DEFAULT_GROUP_NAME = "Unprotected";
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
                + "WHERE NOT EXISTS (SELECT 1 FROM groups WHERE title = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(defaultGroup)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, DEFAULT_GROUP_NAME);
            pstmt.setString(3, DEFAULT_GROUP_NAME);

            pstmt.executeUpdate();
        }

        // adds every currently existing person to the group
        // if they aren't already in it
        String addAll = "UPDATE ? SET instructors = ?, students = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addAll)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, ""); // TODO: list of instructors
            pstmt.setString(3, ""); // TODO: list of students
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
     * Adds a student to a specific group
     * 
     * @param title Unique title of the group
     * @param student Student to add to the group
     */
    public void addStudent(String title, String student) {
        String studentListString = null;
        // initial query to grab the list of students in a group
        String getStudent = "SELECT students FROM ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getStudent)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, title);

            ResultSet studentResultSet = pstmt.executeQuery();

            // adds student to the current string of students
            studentListString = addItemToString(studentResultSet.getString("students"), student);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String addStudent = "UPDATE ? SET students = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addStudent)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, studentListString);
            pstmt.setString(3, title);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes a student from a specific group
     * 
     * @param title Unique title of the group
     * @param student Student to remove from the group
     */
    public void removeStudent(String title, String student) {
        String studentListString = null;
        String getStudents = "SELECT students FROM ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(getStudents)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, title);

            ResultSet studentResultSet = pstmt.executeQuery();

            studentListString = removeItemFromString(studentResultSet.getString("students"), student);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String removeStudent = "UPDATE ? SET students = ? WHERE title = ?"; 

        try (PreparedStatement pstmt = connection.prepareStatement(removeStudent)) {
            pstmt.setString(1, TABLE_NAME);
            pstmt.setString(2, studentListString);
            pstmt.setString(3, title); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds an admin to a group
     * When calling this function, it should be ensured that only a current
     * admin of the group can make other admins
     * 
     * @param title Unique title of the group
     * @param admin Admin to add to the group
     */
    public void addAdmin(String title, String admin) {
        String addAdmin = "UPDATE ? SET admins = ? WHERE title = ?"; // TODO: fix it

        try (PreparedStatement pstmt = connection.prepareStatement(addAdmin)) {

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes an admin from a group
     * Implements a check to ensure that, by removing someone as an admin, a group
     * will not be without admins
     * 
     * @param title Unique title of the group
     * @param admin Admin to remove from the group
     */
    public void removeAdmin(String title, String admin) {
        String removeAdmin = ""; // TODO: fix it

        try (PreparedStatement pstmt = connection.prepareStatement(removeAdmin)) {

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds an article to a group
     * This should make it so that anyone within the group
     * can decrypt the data within the articles
     * 
     * @param title Unique title of the group
     * @param articleID The ID of the article to add to the group
     */
    public void addArticle(String title, int articleID) {
        String addArticle = ""; // TODO: fix it

        try (PreparedStatement pstmt = connection.prepareStatement(addArticle)) {

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Removes an article from a group
     * 
     * @param title Unique title of the group
     * @param articleID The ID of the article to remove from the group
     */
    public void removeArticle(String title, int articleID) {
        String removeArticle = ""; // TODO: fix it

        try (PreparedStatement pstmt = connection.prepareStatement(removeArticle)) {

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
