package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

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
        	stage.setScene(Back.back(stage));
        	
        });

        // Add a loading message while the merging is happening
        getChildren().addAll(new Label("Merged!"), back);

        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s);
        stage.setScene(s);
    }
}
