package project;

import java.sql.*;
import java.util.Arrays;
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
    
    // Update an existing help article based on its unique ID
    public void updateHelpArticle(HelpArticle article) throws SQLException {
        String sql = "UPDATE help_articles SET level = ?, groupIdentifier = ?, access = ?, title = ?, " +
                     "shortDescription = ?, keywords = ?, body = ?, referenceLinks = ?, " +
                     "sensitiveTitle = ?, sensitiveDescription = ?, updatedDate = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, article.getLevel());
            pstmt.setString(2, article.getGroupIdentifier());
            pstmt.setString(3, article.getAccess());
            pstmt.setString(4, article.getTitle());
            pstmt.setString(5, article.getShortDescription());
            Object[] keywordsArray = article.getKeywords();
            String[] stringKeywords = Arrays.stream(keywordsArray)
                                            .map(Object::toString) // Convert each object to a String
                                            .toArray(String[]::new); // Collect as String[]

            pstmt.setString(6, String.join(",", stringKeywords)); // Store keywords as a comma-separated string
            pstmt.setString(7, article.getBody());
         // Convert Object[] to String[]
            Object[] referenceLinksArray = article.getReferenceLinks();
            String[] stringReferences = Arrays.stream(referenceLinksArray)
                                               .map(Object::toString) // Convert each object to a String
                                               .toArray(String[]::new); // Collect as String[]

            pstmt.setString(8, String.join(",", stringReferences)); // Store reference links as a comma-separated string
            pstmt.setString(9, article.getSensitiveTitle());
            pstmt.setString(10, article.getSensitiveDescription());
            pstmt.setTimestamp(11, Timestamp.from(Instant.now())); // Set updated date to current time
            pstmt.setLong(12, article.getId()); // Specify which article to update
            pstmt.executeUpdate();
        }
    }
    
 // Example method to fetch an article by title
    public HelpArticle fetchArticleByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM help_articles WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String level = rs.getString("level");
                    String groupIdentifier = rs.getString("groupIdentifier");
                    String access = rs.getString("access");
                    String shortDescription = rs.getString("shortDescription");
                    Object[] keywords = (Object[]) rs.getArray("keywords").getArray();
                    String body = rs.getString("body");
                    Object[] referenceLinks = (Object[]) rs.getArray("referenceLinks").getArray();
                    String sensitiveTitle = rs.getString("sensitiveTitle");
                    String sensitiveDescription = rs.getString("sensitiveDescription");

                    return new HelpArticle(id, level, groupIdentifier, access, title, shortDescription, keywords, body, referenceLinks, sensitiveTitle, sensitiveDescription);
                }
            }
        }
        return null; // Return null if no article is found
    }

    // Additional methods (e.g., update, delete, etc.) can be added here
}
