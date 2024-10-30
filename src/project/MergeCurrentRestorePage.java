package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;

public class MergeCurrentRestorePage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public MergeCurrentRestorePage(Stage stage, HelpArticleDatabase helpArticleDatabase, String filename) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Merge with Current Articles");

        // Load and merge articles from the backup file
        try {
            helpArticleDatabase.mergeBackupArticles(filename); // Assuming this method handles the merging logic
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Articles merged successfully!", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error merging articles: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

        // Add a loading message while the merging is happening
        getChildren().add(new Label("Merging articles..."));
        stage.setScene(new Scene(this, 400, 200));
    }
}
