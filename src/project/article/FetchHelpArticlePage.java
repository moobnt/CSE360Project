package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.util.Back;

import java.sql.SQLException;

/**
 * <p> FetchHelpArticlePage class </p>
 * 
 * <p> Description: Fetches the article specified by the user </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


/**
 * 
 * 
 * @param ArticleName
 * @returns Article not found
 * 
 */

public class FetchHelpArticlePage extends TilePane {
    public FetchHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Fetch Help Article");

        // Create a GridPane for the fetch form
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Horizontal spacing between columns
        gridPane.setVgap(10); // Vertical spacing between rows

        Label titleLabel = new Label("Title of Article to Update:");
        TextField titleField = new TextField();
        
        Button fetchButton = new Button("Fetch Article");
        fetchButton.setOnAction(e -> {
            String titleToFetch = titleField.getText();
            if (!titleToFetch.isEmpty()) {
                try {
                    HelpArticle articleToUpdate = helpArticleDatabase.fetchArticleByTitle(titleToFetch);
                    if (articleToUpdate != null) {
                        // Redirect to UpdateHelpArticlePage with fetched article
                        new UpdateHelpArticlePage(stage, helpArticleDatabase, articleToUpdate);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Article not found!", ButtonType.OK);
                        alert.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching article: " + ex.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a title to fetch the article!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	Back.back(stage);
        	
        });

        // Add controls to the grid
        gridPane.add(titleLabel, 0, 0);
        gridPane.add(titleField, 1, 0);
        gridPane.add(fetchButton, 1, 1);
        gridPane.add(back, 2, 0);

        // Add the gridPane to the TilePane
        getChildren().add(gridPane);

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s, "Fetch Help Article");
        stage.setScene(s);
        stage.show();
    }
}
