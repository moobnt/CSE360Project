package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

                    // Convert SQL Array to Java Object[]
                    Array keywordsArray = rs.getArray("keywords");
                    Object[] keywords = keywordsArray != null ? (Object[]) keywordsArray.getArray() : new String[0];
                    
                    String body = rs.getString("body");
                    Array referenceLinksArray = rs.getArray("referenceLinks");
                    Object[] referenceLinks = referenceLinksArray != null ? (Object[]) referenceLinksArray.getArray() : new String[0];
                    
                    String sensitiveTitle = rs.getString("sensitiveTitle");
                    String sensitiveDescription = rs.getString("sensitiveDescription");

                    return new HelpArticle(id, level, groupIdentifier, access, title, shortDescription, keywords, body, referenceLinks, sensitiveTitle, sensitiveDescription);
                }
            }
        }
        return null; // Return null if no article is found
    }
    
    public List<HelpArticle> getAllArticles() throws SQLException {
        String sql = "SELECT * FROM help_articles";
        List<HelpArticle> articles = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String level = rs.getString("level");
                String groupIdentifier = rs.getString("groupIdentifier");
                String access = rs.getString("access");
                String shortDescription = rs.getString("shortDescription");

                // Handle keywords and reference links properly
                Object[] keywords = (Object[]) rs.getArray("keywords").getArray();
                Object[] referenceLinks = (Object[]) rs.getArray("referenceLinks").getArray();

                // Add the HelpArticle to the list
                articles.add(new HelpArticle(id, level, groupIdentifier, access, 
                    rs.getString("title"), shortDescription, keywords, 
                    rs.getString("body"), referenceLinks, 
                    rs.getString("sensitiveTitle"), rs.getString("sensitiveDescription")));
            }
        }
        return articles;
    }

    public void deleteArticleById(long articleId) throws SQLException {
        String sql = "DELETE FROM help_articles WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        }
    }
    
    public void removeAllArticles() throws SQLException {
        String sql = "DELETE FROM help_articles"; // Adjust the table name as necessary
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }
    
 // Method to read articles from a backup file
    public List<HelpArticle> readArticlesFromFile(File filename) {
        System.out.println("READING ARTICLES FROM BACKUP FILE");

        List<HelpArticle> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Assuming each article is separated by two newlines and fields are separated by a specific delimiter
                //System.out.println("CURRENT LINE: " + line);
                String[] fields = line.split(";"); // Delimeter is semicolon in BackupArticles

                System.out.println("Length of fields: " + fields.length);

                if (fields.length == 13) { // Adjust according to your fields
                    HelpArticle article = new HelpArticle(
                        Long.parseLong(fields[0].substring(4)), // ID
                        fields[1], // Level
                        fields[2], // Group Identifier
                        fields[3], // Access
                        fields[4], // Title
                        fields[5], // Short Description
                        fields[6].split(","), // Keywords
                        fields[7], // Body
                        fields[8].split(","), // Reference Links
                        fields[9], // Sensitive Title
                        fields[10] // Sensitive Description
                    );
                    System.out.println("ARTICLE CREATED");

                    // Optional: Set created and updated date if needed
                    article.setCreatedDate(Instant.parse(fields[11].substring(14))); // Assuming the date is stored as an ISO-8601 string
                    // TODO: parsing error ^^^^^^^^^^^^^
                    article.setUpdatedDate(Instant.now()); // Set to now or use from file if available

                    System.out.println("AAAAAAAAAAAA");
                    articles.add(article);
                }
                System.out.println("FIELDS: " + fields);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle number format errors if parsing ID fails
        }
        return articles; // Return the constructed list of HelpArticle objects
    }
    
    public void restoreArticlesFromBackup(File filename) throws SQLException {
        List<HelpArticle> articles = readArticlesFromFile(filename); // Method to read from the file

        for (HelpArticle article : articles) {
            // Check if the article with the same ID already exists
            if (!articleExists(article.getId())) {
                // Insert the article into the database
                String sql = "INSERT INTO help_articles (id, title, level, groupIdentifier, access, shortDescription, keywords, body, referenceLinks, sensitiveTitle, sensitiveDescription, createdDate, updatedDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setLong(1, article.getId());
                    pstmt.setString(2, article.getTitle());
                    pstmt.setString(3, article.getLevel());
                    pstmt.setString(4, article.getGroupIdentifier());
                    pstmt.setString(5, article.getAccess());
                    pstmt.setString(6, article.getShortDescription());
                    pstmt.setObject(7, article.getKeywords()); // Adjust this based on how you store keywords
                    pstmt.setString(8, article.getBody());
                    pstmt.setObject(9, article.getReferenceLinks()); // Adjust this based on how you store reference links
                    pstmt.setString(10, article.getSensitiveTitle());
                    pstmt.setString(11, article.getSensitiveDescription());
                    pstmt.setTimestamp(12, Timestamp.from(article.getCreatedDate()));
                    pstmt.setTimestamp(13, Timestamp.from(article.getUpdatedDate()));
                    pstmt.executeUpdate();
                } catch(Exception FileNotFoundException) {
                	System.out.println("Cannot find file");
                }
            }
        }
    }

    private boolean articleExists(long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM help_articles WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count is greater than 0, the article exists
                }
            }
        }
        return false;
    }

    public void mergeBackupArticles(File filename) throws SQLException, Exception {
        List<HelpArticle> articles = readArticlesFromFile(filename); // Read articles from backup
        System.out.println("MERGING BACKUPS");

        for (HelpArticle article : articles) {
        	System.out.println("CHECKING ARTICLE");
            // Check if the article with the same ID already exists
            if (!articleExists(article.getId())) {
                // Insert the article into the database
                String sql = "INSERT INTO help_articles (id, title, level, groupIdentifier, access, shortDescription, keywords, body, referenceLinks, sensitiveTitle, sensitiveDescription, createdDate, updatedDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setLong(1, article.getId());
                    pstmt.setString(2, article.getTitle());
                    pstmt.setString(3, article.getLevel());
                    pstmt.setString(4, article.getGroupIdentifier());
                    pstmt.setString(5, article.getAccess());
                    pstmt.setString(6, article.getShortDescription());
                    pstmt.setObject(7, article.getKeywords()); // Adjust this based on how you store keywords
                    pstmt.setString(8, article.getBody());
                    pstmt.setObject(9, article.getReferenceLinks()); // Adjust this based on how you store reference links
                    pstmt.setString(10, article.getSensitiveTitle());
                    pstmt.setString(11, article.getSensitiveDescription());
                    pstmt.setTimestamp(12, Timestamp.from(article.getCreatedDate()));
                    pstmt.setTimestamp(13, Timestamp.from(article.getUpdatedDate()));
                    pstmt.executeUpdate();
                } catch(Exception e) {
                	Alert alert = new Alert(Alert.AlertType.ERROR, "Error inserting articles: " + e.getMessage(), ButtonType.OK);
                }
            } else {
                // Optionally, you can log or print a message indicating that the article was skipped
                System.out.println("Article with ID " + article.getId() + " already exists, skipping.");
            }
        }
    }
    // Additional methods (e.g., update, delete, etc.) can be added here
}
