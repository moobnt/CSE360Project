package project.article.backup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.article.HelpArticleDatabase;
import project.util.Back;

import java.io.File;
import java.sql.SQLException;

/**
 * <p> MergeCurrRestorePage class </p>
 * 
 * <p> Description: Handles the merging of files for backing up articles. </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class MergeCurrentRestorePage extends VBox {
    public MergeCurrentRestorePage(Stage stage, HelpArticleDatabase helpArticleDatabase, File filename) {
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
        } catch (Exception e) {
        	Alert alert = new Alert(Alert.AlertType.ERROR, "Error merging articles: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
		}

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	Back.back(stage);
        	
        });

        // Add a loading message while the merging is happening
        getChildren().addAll(new Label("Merged!"), back);

        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s, "Merge with Current Articles");
        stage.setScene(s);
    }
}
