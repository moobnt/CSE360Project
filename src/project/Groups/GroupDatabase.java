package project.Groups;

import java.sql.Statement;
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

    public GroupDatabase() {
        connect();

        try {
            createTables();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        String groupTable = "CREATE TABLE IF NOT EXISTS groups ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "title VARCHAR(255) UNIQUE NOT NULL, "
            + "admin VARCHAR(255), "
            + "instructors VARCHAR(255), "
            + "students VARCHAR(65535), "
            + "articles VARCHAR(255))";

        stmt = connection.createStatement();
        stmt.execute(groupTable);
    }
}
