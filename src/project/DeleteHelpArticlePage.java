package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class DeleteHelpArticlePage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public DeleteHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase, String title) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Delete Article(s) with Title: " + title);

        // Create a List of HelpArticles with the specified title
        try {
            List<HelpArticle> articles = helpArticleDatabase.getAllArticles(); // Fetch all articles
            VBox articlesBox = new VBox();
            articlesBox.setSpacing(10); // Add spacing between articles

            // Create radio buttons for each article
            ToggleGroup toggleGroup = new ToggleGroup();
            for (HelpArticle article : articles) {
                if (article.getTitle().equalsIgnoreCase(title)) { // Check if the article title matches
                    RadioButton radioButton = new RadioButton(
                            String.format("Title: %s\nShort Description: %s\nBody: %s",
                                    article.getTitle(),
                                    article.getShortDescription(),
                                    article.getBody()));
                    radioButton.setUserData(article.getId()); // Set user data to the article ID
                    radioButton.setToggleGroup(toggleGroup); // Add to the toggle group
                    articlesBox.getChildren().add(radioButton); // Add radio button to the VBox
                }
            }

            // Button to confirm deletion
            Button deleteButton = new Button("Delete Selected Article");
            deleteButton.setOnAction(e -> {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                if (selectedRadioButton != null) {
                    long selectedArticleId = (long) selectedRadioButton.getUserData(); // Get the article ID
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, 
                        "Are you sure you want to delete this article?", ButtonType.YES, ButtonType.NO);
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try {
                                helpArticleDatabase.deleteArticleById(selectedArticleId); // Delete article by ID
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article deleted successfully!", ButtonType.OK);
                                alert.showAndWait();
                                // Optionally, refresh the article list or close the window
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Error deleting article: " + ex.getMessage(), ButtonType.OK);
                                alert.showAndWait();
                            }
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an article to delete!", ButtonType.OK);
                    alert.showAndWait();
                }
            });

            // Add components to the VBox
            articlesBox.getChildren().add(deleteButton);
            getChildren().add(articlesBox);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading articles: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

        // Set the scene with the current VBox
        stage.setScene(new Scene(this, 600, 400)); // Increase the window size for better readability
        stage.show();
    }
}
