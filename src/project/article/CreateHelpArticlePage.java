package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.util.Back;

/**
 * <p> CreateHelpArticlePage class </p>
 * 
 * <p> Description: Handles the page of the help articles, takes in the user inputs and stores it within the 
 * helpArticlesDatabase  </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class CreateHelpArticlePage extends TilePane {
    public CreateHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase, String username, boolean isInstructor) {
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

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();

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
            String author = authorField.getText();
            String access = accessField.getText();
            String title = titleField.getText();
            String shortDescription = shortDescriptionField.getText();
            String[] keywords = keywordsField.getText().split(",");
            String body = bodyField.getText();
            String[] referenceLinks = referenceLinksField.getText().split(",");

            // Create the HelpArticle object
            HelpArticle article = new HelpArticle(0, level, groupIdentifier, author, access, title, shortDescription, keywords, body, referenceLinks, "", "");

            // Generate a unique ID for the article
            long uniqueId = article.generateUniqueId();
            article.setId(uniqueId);

            // After article creation, redirect to group selection page
            new SelectGroupPage(stage, article, helpArticleDatabase, username, isInstructor);
        });
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
            stage.setScene(Back.back(stage)); // Navigate back
        });

        // Add all labels and fields to the grid
        gridPane.add(levelLabel, 0, 0);
        gridPane.add(levelField, 1, 0);
        gridPane.add(groupIdentifierLabel, 0, 1);
        gridPane.add(groupIdentifierField, 1, 1);
        gridPane.add(authorLabel, 0, 2);
        gridPane.add(authorField, 1, 2);
        gridPane.add(accessLabel, 0, 3);
        gridPane.add(accessField, 1, 3);
        gridPane.add(titleLabel, 0, 4);
        gridPane.add(titleField, 1, 4);
        gridPane.add(shortDescriptionLabel, 0, 5);
        gridPane.add(shortDescriptionField, 1, 5);
        gridPane.add(keywordsLabel, 0, 6);
        gridPane.add(keywordsField, 1, 6);
        gridPane.add(bodyLabel, 0, 7);
        gridPane.add(bodyField, 1, 7);
        gridPane.add(referenceLinksLabel, 0, 8);
        gridPane.add(referenceLinksField, 1, 8);
        gridPane.add(new TilePane(submitButton, back), 1, 9); // Add button at the last row

        // Add the gridPane to the TilePane
        getChildren().addAll(gridPane);

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 500, 600);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
