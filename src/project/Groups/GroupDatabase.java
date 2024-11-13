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

    public void createGroup() {

    }

    public void editGroup() {

    }

    public void deleteGroup() {

    }

    public void addStudent() {

    }

    public void removeStudent() {

    }

    public void addAdmin() {

    }

    public void removeAdmin() {

    }

    public void addArticle() {

    }

    public void removeArticle() {

    }
}
