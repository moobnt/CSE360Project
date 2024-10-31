package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * <p> BackUpOptionsPage class </p>
 * 
 * <p> Description: This handles the page view up the back up options, sending the user to the 
 * appropriate page based on their input </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
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

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Select Backup Option:"), backupAllButton, backupGroupButton, back);

        // Set the scene with the current VBox
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
