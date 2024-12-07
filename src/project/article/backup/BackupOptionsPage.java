package project.article.backup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.article.HelpArticleDatabase;
import project.util.Back;


/**
 * <p> BackUpOptionsPage class </p>
 * 
 * <p> Description: This handles the page view up the back up options, sending the user to the 
 * appropriate page based on their input </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class BackupOptionsPage extends VBox {

    public BackupOptionsPage(Stage stage, HelpArticleDatabase helpArticleDatabase) { // Store the database instance
        stage.setTitle("Backup Options");

        // Create buttons for backup options
        Button backupAllButton = new Button("Backup All Articles");
        backupAllButton.setOnAction(event -> {
            // Navigate to BackupHelpArticlesPage
            new BackupHelpArticlesPage(stage, helpArticleDatabase);
        });

        Button backupGroupButton = new Button("Backup Articles by Group");
        backupGroupButton.setOnAction(event -> {
            // Navigate to BackupArticlesByGroupPage
            new BackupArticlesByGroupPage(stage, helpArticleDatabase);
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	Back.back(stage);
        	
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Select Backup Option:"), backupAllButton, backupGroupButton, back);

        // Set the scene with the current VBox
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s, "Backup Options");
        stage.setScene(s);
        stage.show();
    }
}
