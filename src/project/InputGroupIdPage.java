package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InputGroupIdPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public InputGroupIdPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase;
        stage.setTitle("Input Group ID");

        Label instructionLabel = new Label("Enter Group ID:");
        TextField groupIDField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            String groupID = groupIDField.getText().trim();
            if (!groupID.isEmpty()) {
                ListArticlesByGroupPage listArticlesByGroupPage = new ListArticlesByGroupPage(stage, helpArticleDatabase, groupID);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid group ID.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        getChildren().addAll(instructionLabel, groupIDField, submitButton);
        stage.setScene(new Scene(this, 300, 200));
        stage.show();
    }
}
