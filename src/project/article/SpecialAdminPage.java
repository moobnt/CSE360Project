package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.util.Back;
import project.account.DatabaseHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Insets;

public class SpecialAdminPage {
    public SpecialAdminPage(Stage stage, HelpArticleDatabase helpArticleDatabase, String groupName) {
        stage.setTitle("Special Access Group Admin - " + groupName);

        // Create the admin page layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label groupLabel = new Label("Group: " + groupName);
        gridPane.add(groupLabel, 0, 0);

        // Button to list articles in the special access group
        Button listArticlesButton = new Button("List All Articles");
        listArticlesButton.setOnAction(event -> {
            try {
                listArticlesInGroup(helpArticleDatabase, groupName, gridPane);
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error fetching articles: " + e.getMessage());
            }
        });
        gridPane.add(listArticlesButton, 0, 1);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            Back.back(stage);
        });
        gridPane.add(backButton, 0, 2);

        // Set the scene with the current GridPane
        Scene scene = new Scene(gridPane, 500, 500);
        Back.pushBack(scene, "Special Access Group Admin - " + groupName);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * List all articles in the given group and display them sequentially with decrypted content.
     *
     * @param helpArticleDatabase The HelpArticleDatabase instance to fetch articles.
     * @param groupName           The name of the special access group.
     * @param gridPane            The GridPane where the articles will be displayed.
     * @throws SQLException If there is an issue fetching the articles from the database.
     */
    private void listArticlesInGroup(HelpArticleDatabase helpArticleDatabase, String groupName, GridPane gridPane) throws SQLException {
        // Fetch articles from the database
        List<HelpArticle> articles = helpArticleDatabase.getArticlesByGroup(groupName, "special_access");

        int row = 2;  // Start displaying articles from row 2 (below the buttons)

        // Clear any previous list
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 1);

        // Display the articles
        for (HelpArticle article : articles) {
            String title = article.getTitle();
            String encryptedBody = article.getBody();

            // Decrypt the body (assuming you have an EncryptionUtil class to handle decryption)
            String decryptedBody = null;
            try {
                decryptedBody = EncryptionUtil.decrypt(encryptedBody);  // Decrypt the article body
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Display the article title and body
            Label articleLabel = new Label("Article: " + title);
            TextArea articleBody = new TextArea(decryptedBody);
            articleBody.setEditable(false);
            articleBody.setWrapText(true);

            // Add the article title and body to the GridPane
            gridPane.add(articleLabel, 0, row);
            gridPane.add(articleBody, 0, row + 1);

            row += 2;  // Move to the next available row for the next article
        }

        // If no articles found for the group name
        if (articles.isEmpty()) {
            Label noArticlesLabel = new Label("No articles found for group: " + groupName);
            gridPane.add(noArticlesLabel, 0, row);
        }
    }

    /**
     * Display an error message in an alert.
     *
     * @param message The message to display in the error alert.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
