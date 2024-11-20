package project.article.backup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.article.HelpArticle;
import project.article.HelpArticleDatabase;
import project.util.Back;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p> BackUpArticlesByGroup class </p>
 * 
 * <p> Description: This class handles backing up articles based on their assigned group(designated by user) </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */



public class BackupArticlesByGroupPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;
    private File backupFile = null;

    /**
     * <p>This is the initialization and handler of the back up group page that the user views
     * 	It handles all the inputs the user need for the BackUpArticlesByGroup page
     *  </p>
     * 
     */
    
    public BackupArticlesByGroupPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Backup Articles by Group");

        // Create a TextField for the group names
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Enter group names (comma-separated)");

        // Create a TextField for the filename
        

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.getExtensionFilters().addAll(
        	new ExtensionFilter("Text Files", "*.txt"));

        Button openFileChooserButton = new Button();
        openFileChooserButton.setText("Save as...");
        openFileChooserButton.setOnAction(event -> {
            // if the file doesn't exist, the file will remain null
            // there are checks in other functions for this (or there should be)
            backupFile = fileChooser.showSaveDialog(stage);
        });

        // Create a Button to trigger the backup
        Button backupButton = new Button("Backup Group Articles");
        backupButton.setOnAction(event -> {
            String groupNames = groupNameField.getText();
            if (groupNames.isEmpty() || backupFile == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter valid group names and a filename.", ButtonType.OK);
                alert.showAndWait();
            } else {
                backupArticlesByGroups(groupNames, backupFile);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Backup Articles by Group"), groupNameField, openFileChooserButton, backupButton, back);

        // Set the scene with the current VBox
        Scene s = new Scene(this, 400, 250);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
    /**
     * <p> This takes all the inputs given by the user (group ID) and backs up the appropriate groups </p>
     * 
     */
    private void backupArticlesByGroups(String groupNames, File backupFile) {
        try {
            List<HelpArticle> articles = helpArticleDatabase.getAllArticles(); // Fetch all articles
            List<String> groupIdList = Arrays.stream(groupNames.split(","))
                                              .map(String::trim)
                                              .collect(Collectors.toList());

            StringBuilder backupContent = new StringBuilder();
            boolean articlesFound = false;

            // Filter articles based on the group IDs
            for (HelpArticle article : articles) {
                String[] articleGroupIds = article.getGroupIdentifierArray(); // Get group IDs for the article

                // Check if the article belongs to all specified group IDs
                boolean belongsToAllGroups = groupIdList.stream()
                        .allMatch(groupId -> Arrays.asList(articleGroupIds).contains(groupId));

                if (belongsToAllGroups) {
                    // Prepare article details for backup
                    backupContent.append("ID: ").append(article.getId()).append("; ")
                                 .append("Title: ").append(article.getTitle()).append("; ")
                                 .append("Level: ").append(article.getLevel()).append("; ")
                                 .append("Group Identifier: ").append(article.getGroupIdentifier()).append("; ")
                                 .append("Access: ").append(article.getAccess()).append("; ")
                                 .append("Short Description: ").append(article.getShortDescription()).append("; ")
                                 .append("Keywords: ").append(Arrays.toString(article.getKeywords())).append("; ")
                                 .append("Body: ").append(article.getBody()).append("; ")
                                 .append("Reference Links: ").append(Arrays.toString(article.getReferenceLinks())).append("; ")
                                 .append("Sensitive Title: ").append(article.getSensitiveTitle()).append("; ")
                                 .append("Sensitive Description: ").append(article.getSensitiveDescription()).append("; ")
                                 .append("Created Date: ").append(article.getCreatedDate()).append("; ")
                                 .append("Updated Date: ").append(article.getUpdatedDate()).append("\n");
                    articlesFound = true;
                }
            }

            // Check if any articles were found for the backup
            if (articlesFound) {
                // Specify the file to write to
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
                    writer.write(backupContent.toString());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Backup completed successfully!", ButtonType.OK);
                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error writing to file: " + e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No articles found for the specified groups.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching articles: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}
