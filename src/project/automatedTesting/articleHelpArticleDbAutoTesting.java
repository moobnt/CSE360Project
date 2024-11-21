package project.automatedTesting;

import java.sql.SQLException;
import java.util.List;

import project.article.HelpArticle;
import project.article.HelpArticleDatabase;

public class articleHelpArticleDbAutoTesting {
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) throws SQLException {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nHelp Article Database Automated Testing");
        
        // Initialize database
        HelpArticleDatabase database = new HelpArticleDatabase();
        try {

            database.connect();
            
            // Test Case 1: Create Help Article
            performCreateArticleTest(database);
            
            // Test Case 2: Fetch Article by Title
            performFetchArticleTest(database);
            
            // Test Case 3: Update Help Article
            performUpdateArticleTest(database);
            
            // Test Case 4: Get Articles by Group
            performGetArticlesByGroupTest(database);
            
            // Test Case 5: Delete Article
            performDeleteArticleTest(database);
            
        } catch (Exception e) {
            System.err.println("Critical test failure: " + e.getMessage());
            numFailed++;
        } finally {
            try {
                // Disconnect to prevent connection leaks
                database.disconnect();
            } catch (Exception e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
            }

        // Final Test Summary
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTest Summary:");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performCreateArticleTest(HelpArticleDatabase database) {
        System.out.println("\nTest Case 1: Create Help Article");
        try {
            HelpArticle article = new HelpArticle(
                1001L, 
                "Beginner", 
                "TestGroup", 
                "Author",
                "Public", 
                "Test Article", 
                "A short description", 
                new String[]{"test", "automation"}, 
                "Article body content", 
                new String[]{"https://example.com"}, 
                "Sensitive Title", 
                "Sensitive Description"
            );
            
            database.createHelpArticle(article);
            
            // Verify article was created by fetching it
            HelpArticle fetchedArticle = database.fetchArticleByTitle("Test Article");
            
            if (fetchedArticle != null && fetchedArticle.getTitle().equals("Test Article")) {
                System.out.println("***Success*** Article creation test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Article creation test failed.");
                numFailed++;
            }
        } catch (SQLException e) {
            System.err.println("Article creation test error: " + e.getMessage());
            numFailed++;
        }
    }

    private static void performFetchArticleTest(HelpArticleDatabase database) {
        System.out.println("\nTest Case 2: Fetch Article by Title");
        try {
            HelpArticle article = database.fetchArticleByTitle("Test Article");
            
            if (article != null && "Test Article".equals(article.getTitle())) {
                System.out.println("***Success*** Article fetch test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Article fetch test failed.");
                numFailed++;
            }
        } catch (SQLException e) {
            System.err.println("Article fetch test error: " + e.getMessage());
            numFailed++;
        }
    }

    private static void performUpdateArticleTest(HelpArticleDatabase database) {
        System.out.println("\nTest Case 3: Update Help Article");
        try {
            HelpArticle article = database.fetchArticleByTitle("Test Article");
            
            // Modify the article
            article.setShortDescription("Updated description");
            database.updateHelpArticle(article);
            
            // Fetch and verify update
            HelpArticle updatedArticle = database.fetchArticleByTitle("Test Article");
            
            if (updatedArticle != null && "Updated description".equals(updatedArticle.getShortDescription())) {
                System.out.println("***Success*** Article update test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Article update test failed.");
                numFailed++;
            }
        } catch (SQLException e) {
            System.err.println("Article update test error: " + e.getMessage());
            numFailed++;
        }
    }

    private static void performGetArticlesByGroupTest(HelpArticleDatabase database) {
        System.out.println("\nTest Case 4: Get Articles by Group");
        try {
            List<HelpArticle> articles = database.getArticlesByGroup("TestGroup", "general");
            
            if (articles != null && !articles.isEmpty()) {
                System.out.println("***Success*** Get articles by group test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Get articles by group test failed.");
                numFailed++;
            }
        } catch (SQLException e) {
            System.err.println("Get articles by group test error: " + e.getMessage());
            numFailed++;
        }
    }

    private static void performDeleteArticleTest(HelpArticleDatabase database) {
        System.out.println("\nTest Case 5: Delete Article");
        try {
            HelpArticle article = database.fetchArticleByTitle("Test Article");
            
            // Delete the article
            database.deleteArticleById(article.getId());
            
            // Try to fetch the deleted article
            HelpArticle deletedArticle = database.fetchArticleByTitle("Test Article");
            
            if (deletedArticle == null) {
                System.out.println("***Success*** Article deletion test passed.");
                numPassed++;
            } else {
                System.out.println("***Failure*** Article deletion test failed.");
                numFailed++;
            }
        } catch (SQLException e) {
            System.err.println("Article deletion test error: " + e.getMessage());
            numFailed++;
        }
    }
}