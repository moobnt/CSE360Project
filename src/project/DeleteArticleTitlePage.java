package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DeleteArticleTitlePage extends VBox {
    public DeleteArticleTitlePage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Delete Article by Title");

        Label instructionLabel = new Label("Enter the title of the article to delete:");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter article title");

        Button submitButton = new Button("View Articles");
        submitButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            if (!title.isEmpty()) {
                new DeleteHelpArticlePage(stage, helpArticleDatabase, title);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a title to search.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        
        Button back = new Button("Back");
        back.setOnAction(backEvent -> {
        	stage.setScene(Back.back(stage));
        });

        getChildren().addAll(instructionLabel, titleField, submitButton, back);

        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
