package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BackupOptionsPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public BackupOptionsPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Backup Options");

        // Create buttons for backup options
        Button backupAllButton = new Button("Backup All Articles");
        backupAllButton.setOnAction(event -> {
            // Navigate to BackupHelpArticlesPage
            BackupHelpArticlesPage backupHelpArticlesPage = new BackupHelpArticlesPage(stage, helpArticleDatabase);
        });

        Button backupGroupButton = new Button("Backup Articles by Group");
        backupGroupButton.setOnAction(event -> {
            // Navigate to BackupArticlesByGroupPage
            BackupArticlesByGroupPage backupArticlesByGroupPage = new BackupArticlesByGroupPage(stage, helpArticleDatabase);
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Select Backup Option:"), backupAllButton, backupGroupButton);

        // Set the scene with the current VBox
        stage.setScene(new Scene(this, 400, 200));
        stage.show();
    }
}
