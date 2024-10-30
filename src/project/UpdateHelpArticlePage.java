package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.Arrays;

public class UpdateHelpArticlePage extends TilePane {
    private HelpArticleDatabase helpArticleDatabase;

    // Constructor that takes a HelpArticle object to update
    public UpdateHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase, HelpArticle articleToUpdate) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Update Help Article");

        // Create a GridPane for the article update form
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal spacing between columns
        gridPane.setVgap(10); // Vertical spacing between rows

        // Create labels and fields for article details
        Label levelLabel = new Label("Level:");
        TextField levelField = new TextField();
        levelField.setPromptText("e.g., beginner");
        levelField.setText(articleToUpdate.getLevel());

        Label groupIdentifierLabel = new Label("Group Identifier:");
        TextField groupIdentifierField = new TextField();
        groupIdentifierField.setText(articleToUpdate.getGroupIdentifier());

        Label accessLabel = new Label("Access:");
        TextField accessField = new TextField();
        accessField.setText(articleToUpdate.getAccess());

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        titleField.setText(articleToUpdate.getTitle());

        Label shortDescriptionLabel = new Label("Short Description:");
        TextArea shortDescriptionField = new TextArea();
        shortDescriptionField.setPromptText("Enter short description...");
        shortDescriptionField.setText(articleToUpdate.getShortDescription());

        Label keywordsLabel = new Label("Keywords:");
        TextArea keywordsField = new TextArea();
        keywordsField.setPromptText("Keywords (comma-separated)");
        // Convert Object[] to String for keywords and set text
        Object[] keywordsArray = articleToUpdate.getKeywords();
        String keywordsString = Arrays.stream(keywordsArray)
                                      .map(Object::toString)
                                      .collect(Collectors.joining(","));
        keywordsField.setText(keywordsString);

        Label bodyLabel = new Label("Body of the Article:");
        TextArea bodyField = new TextArea();
        bodyField.setPromptText("Enter the body of the article...");
        bodyField.setText(articleToUpdate.getBody());

        Label referenceLinksLabel = new Label("Reference Links:");
        TextArea referenceLinksField = new TextArea();
        referenceLinksField.setPromptText("Reference Links (comma-separated)");
        // Convert Object[] to String for reference links and set text
        Object[] referenceLinksArray = articleToUpdate.getReferenceLinks();
        String referencesString = Arrays.stream(referenceLinksArray)
                                        .map(Object::toString)
                                        .collect(Collectors.joining(","));
        referenceLinksField.setText(referencesString);

        Button updateButton = new Button("Update Article");
        updateButton.setOnAction(e -> {
            String level = levelField.getText();
            String groupIdentifier = groupIdentifierField.getText();
            String access = accessField.getText();
            String title = titleField.getText();
            String shortDescription = shortDescriptionField.getText();
            // Convert text fields back to Object[] as needed
            Object[] updatedKeywords = keywordsField.getText().split(",");
            String body = bodyField.getText();
            Object[] updatedReferenceLinks = referenceLinksField.getText().split(",");

            // Create the HelpArticle object for updating
            HelpArticle article = new HelpArticle(articleToUpdate.getId(), level, groupIdentifier, access, title, 
                                                   shortDescription, updatedKeywords, body, updatedReferenceLinks, 
                                                   "", "");

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

        // Add all labels and fields to the grid
        gridPane.add(levelLabel, 0, 0);
        gridPane.add(levelField, 1, 0);
        gridPane.add(groupIdentifierLabel, 0, 1);
        gridPane.add(groupIdentifierField, 1, 1);
        gridPane.add(accessLabel, 0, 2);
        gridPane.add(accessField, 1, 2);
        gridPane.add(titleLabel, 0, 3);
        gridPane.add(titleField, 1, 3);
        gridPane.add(shortDescriptionLabel, 0, 4);
        gridPane.add(shortDescriptionField, 1, 4);
        gridPane.add(keywordsLabel, 0, 5);
        gridPane.add(keywordsField, 1, 5);
        gridPane.add(bodyLabel, 0, 6);
        gridPane.add(bodyField, 1, 6);
        gridPane.add(referenceLinksLabel, 0, 7);
        gridPane.add(referenceLinksField, 1, 7);
        gridPane.add(updateButton, 1, 8); // Add button at the last row

        // Add the gridPane to the TilePane
        getChildren().add(gridPane);

        // Set the scene with the current TilePane
        stage.setScene(new Scene(this, 500, 600));
        stage.show();
    }
}
