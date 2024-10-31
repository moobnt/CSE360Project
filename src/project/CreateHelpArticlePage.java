package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class CreateHelpArticlePage extends TilePane {
    public CreateHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Create Help Article");

        // Create a GridPane for the article creation form
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal spacing between columns
        gridPane.setVgap(10); // Vertical spacing between rows

        // Create labels and fields
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

        Button submitButton = new Button("Create Article");
        submitButton.setOnAction(e -> {
            String level = levelField.getText();
            String groupIdentifier = groupIdentifierField.getText();
            String access = accessField.getText();
            String title = titleField.getText();
            String shortDescription = shortDescriptionField.getText();
            String[] keywords = keywordsField.getText().split(",");
            String body = bodyField.getText();
            String[] referenceLinks = referenceLinksField.getText().split(",");

            // Create the HelpArticle object
            HelpArticle article = new HelpArticle(0, level, groupIdentifier, access, title, shortDescription, keywords, body, referenceLinks, "", "");

            // Generate a unique ID for the article
            long uniqueId = article.generateUniqueId();
            article.setId(uniqueId);

            try {
                // Use helpArticleDatabase to create the article
                helpArticleDatabase.createHelpArticle(article);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article created successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating article: " + ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
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
        gridPane.add(new TilePane(submitButton, back), 1, 8); // Add button at the last row

        // Add the gridPane to the TilePane
        getChildren().addAll(gridPane);

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 500, 600);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
