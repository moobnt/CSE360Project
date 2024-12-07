package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.util.Back;

public class SpecialViewPage {
    public SpecialViewPage(Stage stage, HelpArticleDatabase helpArticleDatabase, String groupName) {
        stage.setTitle("Special Access Group View - " + groupName);

        // Create the view page layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label groupLabel = new Label("Group: " + groupName);
        gridPane.add(groupLabel, 0, 0);

        // View Rights Button
        Button viewRightsButton = new Button("View Access Rights");
        viewRightsButton.setOnAction(event -> {
            // Redirect to a page where the user can view the access rights
            //new ViewAccessRightsPage(stage, helpArticleDatabase, groupName);
        });
        gridPane.add(viewRightsButton, 0, 1);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            Back.back(stage);
        });
        gridPane.add(backButton, 0, 2);

        // Set the scene with the current GridPane
        Scene scene = new Scene(gridPane, 400, 250);
        Back.pushBack(scene, "Special Access Group View - " + groupName);
        stage.setScene(scene);
        stage.show();
    }
}
