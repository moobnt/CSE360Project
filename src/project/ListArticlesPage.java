package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

/**
 * <p> ListArticlePage class </p>
 * 
 * <p> Description: Builds a page for the user to select how they would like to view their articles/list them </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class ListArticlesPage extends TilePane {
    public ListArticlesPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("List Articles");
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        // Create buttons for different article listing options
        Button listAllArticlesButton = new Button("List All Articles");
        listAllArticlesButton.setOnAction(event -> {
            new ListArticlesHelpPage(stage, helpArticleDatabase);
        });

        Button listByGroupButton = new Button("List Articles by Group");
        listByGroupButton.setOnAction(event -> { 
            // Navigate to InputGroupIdPage for user input
            new InputGroupIdPage(stage, helpArticleDatabase);
        });

        Button viewArticleButton = new Button("View Article by Title");
        viewArticleButton.setOnAction(event -> {
        	// Navigate to InputGroupIdPage for user input
        	new InputTitlePage(stage, helpArticleDatabase);
        });

        // Add buttons to the TilePane
        getChildren().addAll(listAllArticlesButton, listByGroupButton, viewArticleButton, back);

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 300, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
