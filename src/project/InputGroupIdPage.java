package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
