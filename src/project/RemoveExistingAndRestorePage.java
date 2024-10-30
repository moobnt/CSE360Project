package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class RemoveExistingAndRestorePage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public RemoveExistingAndRestorePage(Stage stage, HelpArticleDatabase helpArticleDatabase, String filename) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Remove Existing Articles and Restore");

        // Confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "This will remove all existing articles. Do you want to continue?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setHeaderText("Confirm Restoration");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    helpArticleDatabase.removeAllArticles(); // Clear all existing articles
                    helpArticleDatabase.restoreArticlesFromBackup(filename); // Restore from the backup
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Articles restored successfully!", ButtonType.OK);
                    alert.showAndWait();
                    stage.close(); // Close the page
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error restoring articles: " + e.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                stage.close(); // Close if the user selects NO
            }
        });

        // Add a loading message while the restoration is happening
        getChildren().add(new Label("Restoring articles..."));
        stage.setScene(new Scene(this, 400, 200));
    }
}
