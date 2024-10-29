package project;

import java.sql.*;
import java.time.Instant;

public class HelpArticleDatabase {
    private Connection connection;

    public HelpArticleDatabase() {
        connect();
        try {
            createTables(); // Ensure the tables are created on initialization
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Create the help_articles table if it doesn't exist
    private void createTables() throws SQLException {
        String helpArticlesTable = "CREATE TABLE IF NOT EXISTS help_articles ("
                + "id BIGINT PRIMARY KEY, "
                + "level VARCHAR(255), "
                + "groupIdentifier VARCHAR(255), "
                + "access VARCHAR(255), "
                + "title VARCHAR(255), "
                + "shortDescription VARCHAR(255), "
                + "keywords VARCHAR(255), "
                + "body TEXT, "
                + "referenceLinks VARCHAR(255), "
                + "sensitiveTitle VARCHAR(255), "
                + "sensitiveDescription VARCHAR(255), "
                + "createdDate TIMESTAMP, "
                + "updatedDate TIMESTAMP)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(helpArticlesTable);
        }
    }

    private void connect() {
        try {
            connection = DatabaseHelper.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createHelpArticle(HelpArticle article) throws SQLException {
        String sql = "INSERT INTO help_articles (id, level, groupIdentifier, access, title, " +
                     "shortDescription, keywords, body, referenceLinks, sensitiveTitle, sensitiveDescription, createdDate, updatedDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, article.getId());
            pstmt.setString(2, article.getLevel());
            pstmt.setString(3, article.getGroupIdentifier());
            pstmt.setString(4, article.getAccess());
            pstmt.setString(5, article.getTitle());
            pstmt.setString(6, article.getShortDescription());
            pstmt.setArray(7, connection.createArrayOf("VARCHAR", article.getKeywords()));
            pstmt.setString(8, article.getBody());
            pstmt.setArray(9, connection.createArrayOf("VARCHAR", article.getReferenceLinks()));
            pstmt.setString(10, article.getSensitiveTitle());
            pstmt.setString(11, article.getSensitiveDescription());
            pstmt.setTimestamp(12, Timestamp.from(article.getCreatedDate()));
            pstmt.setTimestamp(13, Timestamp.from(article.getUpdatedDate()));
            pstmt.executeUpdate();
        }
    }

    // Additional methods (e.g., update, delete, etc.) can be added here
}
