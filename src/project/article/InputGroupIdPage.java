package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.util.Back;

/**
 * <p> InputGroupIdPage class </p>
 * 
 * <p> Description: handles the user input for group id </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


/**
 * 
 * @param Id user enters group ID
 * @return "Please enter a valid group ID."
 * @return list of articles based on id
 */

public class InputGroupIdPage extends VBox {
    public InputGroupIdPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Input Group ID");

        Label instructionLabel = new Label("Enter Group ID:");
        TextField groupIDField = new TextField();
        Button submitButton = new Button("Submit");
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        submitButton.setOnAction(event -> {
            String groupID = groupIDField.getText().trim();
            if (!groupID.isEmpty()) {
                new ListArticlesByGroupPage(stage, helpArticleDatabase, groupID);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid group ID.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        getChildren().addAll(instructionLabel, groupIDField, submitButton, back);
        Scene s = new Scene(this, 300, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
