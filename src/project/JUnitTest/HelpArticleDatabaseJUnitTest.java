package project.JUnitTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import project.article.HelpArticle;
import project.article.HelpArticleDatabase;

public class HelpArticleDatabaseJUnitTest {

    static HelpArticleDatabase database;
    static HelpArticle testArticle;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        database = new HelpArticleDatabase();
        database.connect();
        // Prepare test article
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
    }

    @AfterAll
    public static void tearDown() throws Exception {
        database.disconnect();
    }

    @Test
    public void testCreateArticle() throws SQLException {
        database.createHelpArticle(testArticle);
        HelpArticle fetchedArticle = database.fetchArticleByTitle("Test Article");
        assertNotNull(fetchedArticle, "Article creation test failed: article not found.");
        assertEquals("Test Article", fetchedArticle.getTitle(), "Article title does not match expected.");
    }

    @Test
    public void testFetchArticleByTitle() throws SQLException {
        HelpArticle article = database.fetchArticleByTitle("Test Article");
        assertNotNull(article, "Article fetch test failed: article not found.");
        assertEquals("Test Article", article.getTitle(), "Fetched article title does not match expected.");
    }

    @Test
    public void testUpdateArticle() throws SQLException {
        testArticle.setShortDescription("Updated description");
        database.updateHelpArticle(testArticle);
        HelpArticle updatedArticle = database.fetchArticleByTitle("Test Article");
        assertNotNull(updatedArticle, "Article update test failed: updated article not found.");
        assertEquals("Updated description", updatedArticle.getShortDescription(), "Updated description does not match expected.");
    }

    @Test
    public void testGetArticlesByGroup() throws SQLException {
        List<HelpArticle> articles = database.getArticlesByGroup("TestGroup", "general");
        assertNotNull(articles, "Get articles by group test failed: articles list is null.");
        //test later when article groups is not empty
        //assertFalse(articles.isEmpty(), "Get articles by group test failed: no articles found.");
    }

    @Test
    public void testDeleteArticle() throws SQLException {
        database.deleteArticleById(testArticle.getId());
        HelpArticle deletedArticle = database.fetchArticleByTitle("Test Article");
        assertNull(deletedArticle, "Article deletion test failed: article still found after deletion.");
    }
}