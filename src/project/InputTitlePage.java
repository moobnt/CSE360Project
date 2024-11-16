package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.util.Back;

/**
 * <p> InputTitle page class </p>
 * 
 * <p> Description: Builds/handles the page that user see that take in article titles. Using the ViewHelperPage class it @returns 
 * the article if found </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class InputTitlePage extends VBox {
    public InputTitlePage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Input Article Title");

        Label instructionLabel = new Label("Enter the title of the article:");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter article title");
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        Button submitButton = new Button("View Articles");
        submitButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            if (!title.isEmpty()) {
                new ViewHelpArticlePage(stage, helpArticleDatabase, title);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a title to search.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        getChildren().addAll(instructionLabel, titleField, submitButton, back);
        
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
