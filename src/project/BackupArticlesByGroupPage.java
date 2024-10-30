package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BackupArticlesByGroupPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public BackupArticlesByGroupPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Backup Articles by Group");

        // Create a TextField for the group names
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Enter group names (comma-separated)");

        // Create a TextField for the filename
        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter backup filename (without extension)");

        // Create a Button to trigger the backup
        Button backupButton = new Button("Backup Group Articles");
        backupButton.setOnAction(event -> {
            String groupNames = groupNameField.getText();
            String fileName = fileNameField.getText();
            if (groupNames.isEmpty() || fileName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter valid group names and a filename.", ButtonType.OK);
                alert.showAndWait();
            } else {
                backupArticlesByGroups(groupNames, fileName);
            }
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Backup Articles by Group"), groupNameField, fileNameField, backupButton);

        // Set the scene with the current VBox
        stage.setScene(new Scene(this, 400, 250));
        stage.show();
    }

    private void backupArticlesByGroups(String groupNames, String fileName) {
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
                    backupContent.append("ID: ").append(article.getId()).append("\n")
                                 .append("Title: ").append(article.getTitle()).append("\n")
                                 .append("Level: ").append(article.getLevel()).append("\n")
                                 .append("Group Identifier: ").append(article.getGroupIdentifier()).append("\n")
                                 .append("Access: ").append(article.getAccess()).append("\n")
                                 .append("Short Description: ").append(article.getShortDescription()).append("\n")
                                 .append("Keywords: ").append(Arrays.toString(article.getKeywords())).append("\n")
                                 .append("Body: ").append(article.getBody()).append("\n")
                                 .append("Reference Links: ").append(Arrays.toString(article.getReferenceLinks())).append("\n")
                                 .append("Sensitive Title: ").append(article.getSensitiveTitle()).append("\n")
                                 .append("Sensitive Description: ").append(article.getSensitiveDescription()).append("\n")
                                 .append("Created Date: ").append(article.getCreatedDate()).append("\n")
                                 .append("Updated Date: ").append(article.getUpdatedDate()).append("\n\n");
                    articlesFound = true;
                }
            }

            // Check if any articles were found for the backup
            if (articlesFound) {
                // Specify the file to write to
                String filePath = fileName + ".txt"; // Use the user-defined filename with a .txt extension
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
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
