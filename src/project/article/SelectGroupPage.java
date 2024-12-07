package project.article;

import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.util.Back;


public class SelectGroupPage extends TilePane {
    public SelectGroupPage(Stage stage, HelpArticle article, HelpArticleDatabase helpArticleDatabase, String username, boolean isInstructor) {
        stage.setTitle("Select Group Type");

        // Create buttons for group selection
        Button generalGroupButton = new Button("General Group");
        Button specialAccessGroupButton = new Button("Special Access Group");

        // BACK ---------------------------------------------------------------
        Button back = new Button("Back");
        back.setOnAction(backEvent -> {
            Back.back(stage);
        });

        // Set actions for both buttons
        generalGroupButton.setOnAction(event -> {
            // Ask for the group name for a general group
            TextInputDialog groupNameDialog = new TextInputDialog();
            groupNameDialog.setTitle("General Group");
            groupNameDialog.setHeaderText("Enter the name of the General Group:");
            groupNameDialog.showAndWait().ifPresent(groupName -> {
                article.setGroupIdentifier(groupName);  // Keep the unique group identifier
                String groupNameForStorage = groupName; // Store the group name for future retrieval

                try {
                    // Store the article in the specified general group
                    helpArticleDatabase.createGroupArticle(article, groupNameForStorage, "general", username, isInstructor);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article created successfully in General Group!", ButtonType.OK);
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating article: " + e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            });
        });

        specialAccessGroupButton.setOnAction(event -> {
            // Ask for the group name for a special access group
            TextInputDialog groupNameDialog = new TextInputDialog();
            groupNameDialog.setTitle("Special Access Group");
            groupNameDialog.setHeaderText("Enter the name of the Special Access Group:");
            groupNameDialog.showAndWait().ifPresent(groupName -> {
                article.setGroupIdentifier(groupName); // Keep the unique group identifier
                String groupNameForStorage = groupName; // Store the group name for future retrieval

                try {
                    // Encrypt the body if it's a special access group
                    String encryptedBody = EncryptionUtil.encrypt(article.getBody());
                    article.setBody(encryptedBody); // Set the encrypted body

                    // Store the article in the specified special access group
                    helpArticleDatabase.createGroupArticle(article, groupNameForStorage, "special_access", username, isInstructor);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Article created successfully in Special Access Group!", ButtonType.OK);
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating article: " + e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            });
        });

        // Add buttons to the page layout
        getChildren().addAll(generalGroupButton, specialAccessGroupButton, back);

        // Set the scene for this page
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s, "Select Group Type");
        stage.setScene(s);
        stage.show();
    }
}
