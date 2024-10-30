package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BackupHelpArticlesPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public BackupHelpArticlesPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Backup Help Articles");

        // Create a TextField for the file name
        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter backup file name (e.g., backup.txt)");

        // Create a Button to trigger the backup
        Button backupButton = new Button("Backup Articles");
        backupButton.setOnAction(event -> {
            String fileName = fileNameField.getText();
            if (fileName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid file name.", ButtonType.OK);
                alert.showAndWait();
            } else {
                backupArticles(fileName);
            }
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Backup Help Articles"), fileNameField, backupButton);

        // Set the scene with the current VBox
        stage.setScene(new Scene(this, 400, 200));
        stage.show();
    }

    private void backupArticles(String fileName) {
        try {
            List<HelpArticle> articles = helpArticleDatabase.getAllArticles(); // Fetch all articles
            File backupFile = new File(fileName);
            FileWriter writer = new FileWriter(backupFile);

            for (HelpArticle article : articles) {
                writer.write(articleToString(article) + System.lineSeparator());
            }

            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Backup completed successfully!", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching articles: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error writing to file: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private String articleToString(HelpArticle article) {
        // Convert the HelpArticle object to a String representation
        String keywordsString = Arrays.stream(article.getKeywords())
                                      .map(Object::toString)
                                      .collect(Collectors.joining(", "));
        String referenceLinksString = Arrays.stream(article.getReferenceLinks())
                                            .map(Object::toString)
                                            .collect(Collectors.joining(", "));
        
        return String.format("ID: %d, Title: %s, Level: %s, Group Identifier: %s, Access: %s, Short Description: %s, " +
                             "Keywords: [%s], Body: %s, Reference Links: [%s], Sensitive Title: %s, " +
                             "Sensitive Description: %s, Created Date: %s, Updated Date: %s",
                article.getId(),
                article.getTitle(),
                article.getLevel(),
                article.getGroupIdentifier(),
                article.getAccess(),
                article.getShortDescription(),
                keywordsString,
                article.getBody(),
                referenceLinksString,
                article.getSensitiveTitle(),
                article.getSensitiveDescription(),
                article.getCreatedDate().toString(), // Assuming createdDate is not null
                article.getUpdatedDate().toString()  // Assuming updatedDate is not null
        );
    }

}
