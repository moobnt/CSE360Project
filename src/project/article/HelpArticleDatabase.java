package project.article;

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
import project.account.DatabaseHelper;
import project.account.DatabaseModel;

import java.time.Instant;

/**
 * <p> HelpArticleDatabase class </p>
 * 
 * <p> Description: Builds a data base on existing articles and future articles built using the system. </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */



public class HelpArticleDatabase extends DatabaseModel {
    private static Connection connection;
    // private static Statement statement;

    private static final String JDBC_DRIVER = "org.h2.Driver"; // H2 JDBC driver
    private static final String DB_URL = "jdbc:h2:./database"; // H2 database URL (relative path to database file)
    private static final String USER = "sa"; // Default user for H2
    private static final String PASS = ""; // Default password for H2 (empty string for embedded mode)
    
    public HelpArticleDatabase() throws SQLException {
        connect();
        try {
            createTables(); // Ensure the tables are created on initialization
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Establishes a connection to the database and creates necessary tables.
     * @throws SQLException 
     */
    public void connect() throws SQLException {
        try {
            // Load the JDBC driver for H2
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            // statement = connection.createStatement();
            
            // Create tables if not already present
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }
    
    // Create the help_articles table if it doesn't exist
    private void createTables() throws SQLException {
        // Create the help_articles table if it doesn't exist
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

        // Create the group_articles table if it doesn't exist
        String groupArticlesTable = "CREATE TABLE IF NOT EXISTS group_articles ("
                + "article_id BIGINT, "
                + "group_name VARCHAR(255), "
                + "group_type VARCHAR(50), " // 'general' or 'special_access'
                + "adminRights VARCHAR(255), "  // Store admin rights as a comma-separated string
                + "viewable VARCHAR(255), "      // Store viewable users as a comma-separated string
                + "isInstructor BOOLEAN, "        // Flag to check if the first instructor is added
                + "PRIMARY KEY (article_id, group_name))";

        // Execute the table creation statements
        try (Statement stmt = connection.createStatement()) {
            // Create help_articles table
            stmt.execute(helpArticlesTable);
            // Create group_articles table
            stmt.execute(groupArticlesTable);
         // Check if the adminRights and viewable columns already exist in group_articles table
            String checkColumnsQuery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
                                     + "WHERE TABLE_NAME = 'GROUP_ARTICLES' "
                                     + "AND (COLUMN_NAME = 'ADMINRIGHTS' OR COLUMN_NAME = 'VIEWABLE' OR COLUMN_NAME = 'ISINSTRUCTOR')";

            try (PreparedStatement pstmt = connection.prepareStatement(checkColumnsQuery)) {
                ResultSet rs = pstmt.executeQuery();

                boolean adminRightsExists = false;
                boolean viewableExists = false;
                boolean isInstructorExists = false;

                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    if ("ADMINRIGHTS".equalsIgnoreCase(columnName)) {
                        adminRightsExists = true;
                    } else if ("VIEWABLE".equalsIgnoreCase(columnName)) {
                        viewableExists = true;
                    } else if ("ISINSTRUCTOR".equalsIgnoreCase(columnName)) {
                        isInstructorExists = true;
                    }
                }

                // Add columns if they do not exist
                if (!adminRightsExists) {
                    String alterTableAdminRights = "ALTER TABLE group_articles ADD COLUMN adminRights VARCHAR(255)";
                    stmt.executeUpdate(alterTableAdminRights);
                }

                if (!viewableExists) {
                    String alterTableViewable = "ALTER TABLE group_articles ADD COLUMN viewable VARCHAR(255)";
                    stmt.executeUpdate(alterTableViewable);
                }

                if (!isInstructorExists) {
                    String alterTableIsInstructor = "ALTER TABLE group_articles ADD COLUMN isInstructor BOOLEAN";
                    stmt.executeUpdate(alterTableIsInstructor);
                }
            }
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
 
    // Method to store an article in a specific group with its type (General or Special Access)
    public void createGroupArticle(HelpArticle article, String groupName, String groupType, String username, boolean isInstructor) throws SQLException {
        // Generate a unique ID for the article if not already set
        long uniqueId = article.getId() == 0 ? article.generateUniqueId() : article.getId();
        article.setId(uniqueId); // Ensure the article has an ID
        System.out.println(username);

        // Set initial values for admin rights and viewable users as comma-separated strings
        String adminRights = username; // The user who creates the article is added as an admin
        String viewable = username;    // The user is also initially added to the viewable array

        // Check if the user is the first instructor to be added to adminRights
        boolean isFirstInstructor = false;

        // If the group is special access, check if it's the first instructor
        if ("special_access".equals(groupType) && isInstructor) {
            // Query the group_articles table to check if any instructors already exist
            String checkInstructorQuery = "SELECT isInstructor FROM group_articles WHERE group_name = ? AND group_type = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkInstructorQuery)) {
                checkStmt.setString(1, groupName);
                checkStmt.setString(2, groupType);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    // First instructor for this group
                    isFirstInstructor = true;
                }
            }
        }

        // If the group is special access, store the article with admin rights, viewable rights, and instructor status
        if ("special_access".equals(groupType)) {
        	System.out.println("HELLO???");
            // Insert into group_articles table to map article to the group
            String query = "INSERT INTO group_articles (article_id, group_name, group_type, adminRights, viewable, isInstructor) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, uniqueId);
                stmt.setString(2, groupName);
                stmt.setString(3, groupType);
                stmt.setString(4, adminRights);  // Store admin rights as a comma-separated string
                stmt.setString(5, viewable);    // Store viewable rights as a comma-separated string
                stmt.setBoolean(6, isFirstInstructor);  // Set instructor flag if this is the first instructor
                stmt.executeUpdate();
                
            }
            
            
        }

        // Insert the article itself into the help_articles table (already done through createHelpArticle)
        // The article is stored in the help_articles table using the same method you're already using
        createHelpArticle(article);
    }
    
    public void printALL() throws SQLException {
    	String s = "SELECT * FROM group_articles";
    	try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(s)) {
           while (rs.next()) {
               System.out.println("Name: " + rs.getString("group_name"));
               System.out.println("Viewable: " + rs.getString("viewable"));
               System.out.println("Admin Rights: " + rs.getString("adminRights"));
               System.out.println("---------------------------");
           }
       }
    }

    // Method to retrieve articles by group name
    public List<HelpArticle> getArticlesByGroup(String groupName, String groupType) throws SQLException {
        List<HelpArticle> articles = new ArrayList<>();

        // Query to get articles that belong to a specific group and group type
        String query = "SELECT ha.* FROM help_articles ha "
                     + "JOIN group_articles ga ON ha.id = ga.article_id "
                     + "WHERE ga.group_name = ? AND ga.group_type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, groupName);           // Set the group name
            stmt.setString(2, groupType);           // Set the group type
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HelpArticle article = new HelpArticle(
                        rs.getLong("id"),
                        rs.getString("level"),
                        rs.getString("groupIdentifier"),
                        rs.getString("access"),
                        rs.getString("title"),
                        rs.getString("shortDescription"),
                        (String[]) rs.getArray("keywords").getArray(),
                        rs.getString("body"),
                        (String[]) rs.getArray("referenceLinks").getArray(),
                        rs.getString("sensitiveTitle"),
                        rs.getString("sensitiveDescription")
                );
                System.out.println(article.getTitle());
                articles.add(article);
            }
        }

        return articles;
    }
    
    public static boolean isUserAdminInGroup(String groupName) throws SQLException {
        String username = DatabaseHelper.getUsername();  // Retrieve the current logged-in username
        String query = "SELECT adminRights FROM group_articles WHERE group_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String adminRights = rs.getString("adminRights");
                // Check if the username is in the adminRights (comma-separated string)
                return adminRights != null && Arrays.asList(adminRights.split(",")).contains(username);
            }
        }
        return false;
    }
    
    public static boolean isUserViewableInGroup(String groupName) throws SQLException {
    	System.out.println("test");
        String query = "SELECT viewable FROM group_articles WHERE group_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, groupName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String viewableUsers = rs.getString("viewable");
                if(viewableUsers != null) {
                	String[] viewableArray = viewableUsers.split(",");  // Assuming viewable users are stored as a comma-separated string
                    for (String user : viewableArray) {
                    	System.out.println(user);
                        if (user.trim().equalsIgnoreCase(DatabaseHelper.getUsername())) {
                            return true; // User is in the viewable list
                        }
                    }
                }
                
            }
        }
        return false; // User is not in the viewable list
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
                String[] fields = line.split(";"); // Delimeter is semicolon in BackupArticles

                if (fields.length == 13) { // Adjust according to your fields
                    HelpArticle article = new HelpArticle(
                        Long.parseLong(fields[0].substring(4)), // ID
                        fields[2].substring(8), // Level
                        fields[3].substring(19), // Group Identifier
                        fields[4].substring(9), // Access
                        fields[1].substring(8), // Title
                        fields[5].substring(20), // Short Description
                        fields[6].substring(11).split(","), // Keywords
                        fields[7].substring(7), // Body
                        fields[8].substring(18).split(","), // Reference Links
                        fields[9].substring(18), // Sensitive Title
                        fields[10].substring(24) // Sensitive Description
                    );

                    // Optional: Set created and updated date if needed
                    article.setCreatedDate(Instant.parse(fields[11].substring(15))); // Assuming the date is stored as an ISO-8601 string
                    article.setUpdatedDate(Instant.now()); // Set to now or use from file if available

                    articles.add(article);
                }
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
                	new Alert(Alert.AlertType.ERROR, "Error inserting articles: " + e.getMessage(), ButtonType.OK);
                }
            } else {
                // Optionally, you can log or print a message indicating that the article was skipped
                System.out.println("Article with ID " + article.getId() + " already exists, skipping.");
            }
        }
    }
    // Additional methods (e.g., update, delete, etc.) can be added here
}
