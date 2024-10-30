package project;

import javafx.scene.Scene;
import java.util.stream.Collectors;
import javafx.scene.control.*;
import java.util.Arrays;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class UpdateHelpArticlePage extends TilePane {
    private HelpArticleDatabase helpArticleDatabase;

    public UpdateHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Update Help Article");

        // Create labels and fields
        Label titleToUpdateLabel = new Label("Title of Article to Update:");
        TextField titleToUpdateField = new TextField();
        
        Button fetchButton = new Button("Fetch Article");
        Label levelLabel = new Label("Level:");
        TextField levelField = new TextField();
        levelField.setPromptText("e.g., beginner");

        Label groupIdentifierLabel = new Label("Group Identifier:");
        TextField groupIdentifierField = new TextField();

        Label accessLabel = new Label("Access:");
        TextField accessField = new TextField();
        accessField.setPromptText("e.g., public");

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label shortDescriptionLabel = new Label("Short Description:");
        TextArea shortDescriptionField = new TextArea();
        shortDescriptionField.setPromptText("Enter short description...");

        Label keywordsLabel = new Label("Keywords:");
        TextArea keywordsField = new TextArea();
        keywordsField.setPromptText("Keywords (comma-separated)");

        Label bodyLabel = new Label("Body of the Article:");
        TextArea bodyField = new TextArea();
        bodyField.setPromptText("Enter the body of the article...");

        Label referenceLinksLabel = new Label("Reference Links:");
        TextArea referenceLinksField = new TextArea();
        referenceLinksField.setPromptText("Reference Links (comma-separated)");

        Button updateButton = new Button("Update Article");
        updateButton.setOnAction(e -> {
            String level = levelField.getText();
            String groupIdentifier = groupIdentifierField.getText();
            String access = accessField.getText();
            String title = titleField.getText();
            String shortDescription = shortDescriptionField.getText();
            Object[] keywords = keywordsField.getText().split(",");
            String body = bodyField.getText();
            Object[] referenceLinks = referenceLinksField.getText().split(",");

            // Create the HelpArticle object
            HelpArticle article = new HelpArticle(0, level, groupIdentifier, access, title, shortDescription, keywords, body, referenceLinks, "", "");

            try {
                // Use helpArticleDatabase to update the article
                helpArticleDatabase.updateHelpArticle(article);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article updated successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating article: " + ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });

        fetchButton.setOnAction(e -> {
            String titleToUpdate = titleToUpdateField.getText(); // Get the title of the article to update
            if (!titleToUpdate.isEmpty()) {
                try {
                    HelpArticle articleToUpdate = helpArticleDatabase.fetchArticleByTitle(titleToUpdate);
                    if (articleToUpdate != null) {
                        // Populate fields with the fetched article details
                        levelField.setText(articleToUpdate.getLevel());
                        groupIdentifierField.setText(articleToUpdate.getGroupIdentifier());
                        accessField.setText(articleToUpdate.getAccess());
                        titleField.setText(articleToUpdate.getTitle());
                        shortDescriptionField.setText(articleToUpdate.getShortDescription());
                     // Convert Object[] to String for keywords
                        Object[] keywordsArray = articleToUpdate.getKeywords();
                        String keywordsString = Arrays.stream(keywordsArray)
                                                      .map(Object::toString)
                                                      .collect(Collectors.joining(",")); // Join as a comma-separated string
                        keywordsField.setText(keywordsString); // Set keywordsField with the joined string

                        bodyField.setText(articleToUpdate.getBody());

                        // Convert Object[] to String for reference links
                        Object[] referenceLinksArray = articleToUpdate.getReferenceLinks();
                        String referencesString = Arrays.stream(referenceLinksArray)
                                                        .map(Object::toString)
                                                        .collect(Collectors.joining(",")); // Join as a comma-separated string
                        referenceLinksField.setText(referencesString); // Set referenceLinksField with the joined string
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Article not found!", ButtonType.OK);
                        alert.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching article: " + ex.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a title to fetch the article!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Add all controls to the TilePane
        getChildren().addAll(titleToUpdateLabel, titleToUpdateField, fetchButton,
                levelLabel, levelField, groupIdentifierLabel, groupIdentifierField,
                accessLabel, accessField, titleLabel, titleField,
                shortDescriptionLabel, shortDescriptionField, keywordsLabel, keywordsField,
                bodyLabel, bodyField, referenceLinksLabel, referenceLinksField, updateButton);

        // Set the scene with the current TilePane
        stage.setScene(new Scene(this, 500, 600));
        stage.show();
    }
}
