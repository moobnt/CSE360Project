package project.Groups;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import project.DatabaseModel;

/**
 * <p> GroupDatabase Class </p>
 * 
 * <p> Description: Class to manage database operations for groups </p>
 * 
 * @version 1.00 2024-11-13 Initial baseline
 */
public class GroupDatabase extends DatabaseModel {
    Statement stmt;
    private final String DEFAULT_GROUP_NAME = "Unprotected";

    /**
     * Default constructor; connects to the databse and creates tables
     */
    public GroupDatabase() {
        connect();

        try {
            createTables();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Sets up the table initially and makes it up-to-date
     * 
     * @throws SQLException
     */
    private void createTables() throws SQLException {
        stmt = connection.createStatement();

        String groupTable = "CREATE TABLE IF NOT EXISTS groups ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "title VARCHAR(255) UNIQUE NOT NULL, "
            + "admin VARCHAR(255), "
            + "instructors VARCHAR(255), "
            + "students VARCHAR(65535), "
            + "articles VARCHAR(255))";

        stmt.execute(groupTable);

        // creates a default group that will not encrypt any articles
        // anyone can access this group, but because it is so simple
        // it does not have an admin
        //
        // Every instructor and student should be in this group
        String defaultGroup = "INSERT INTO groups (title, admin, instructors, students, articles)"
                + "VALUES (?, NULL, NULL, NULL, NULL)"
                + "WHERE NOT EXISTS (SELECT 1 FROM groups WHERE title = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(defaultGroup)) {
            pstmt.setString(1, DEFAULT_GROUP_NAME);
            pstmt.setString(2, DEFAULT_GROUP_NAME);

            pstmt.executeUpdate();
        }

        // adds every currently existing person to the group
        // if they aren't already in it
        String addAll = "UPDATE groups SET instructors = ?, students = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addAll)) {
            pstmt.setString(1, ""); // TODO: list of instructors
            pstmt.setString(2, ""); // TODO: list of students
            pstmt.setString(3, DEFAULT_GROUP_NAME);

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
        String createGroup = "INSERT INTO groups (title, admin, instructors, students, articles) "
                + "VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement pstmt = connection.prepareStatement(createGroup)) {
            pstmt.setString(1, title);
            pstmt.setString(2, admin);
            pstmt.setString(3, instructors);
            pstmt.setString(4, students);
            pstmt.setString(5, articles);

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
        String deleteGroup = "DELETE FROM groups WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteGroup)) {
            pstmt.setString(1, title);

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
        String addStudent = "UPDATE groups SET students = ? WHERE title = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(addStudent)) {

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
        String removeStudent = ""; // pull the comma seperated string and remove the name

        try (PreparedStatement pstmt = connection.prepareStatement(removeStudent)) {

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
        String addAdmin = "UPDATE groups SET admins = ? WHERE title = ?";

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
        String removeAdmin = "";

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
        String addArticle = "";

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
        String removeArticle = "";

        try (PreparedStatement pstmt = connection.prepareStatement(removeArticle)) {

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
