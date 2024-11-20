package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseHelper;
import project.util.Back;

import java.sql.SQLException;

public class SpecialAdminPage {
    public SpecialAdminPage(Stage stage, HelpArticleDatabase helpArticleDatabase, String groupName) {
        stage.setTitle("Special Access Group Admin - " + groupName);

        // Create the admin page layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label groupLabel = new Label("Group: " + groupName);
        gridPane.add(groupLabel, 0, 0);

        // Manage Admin Rights Button
        Button manageAdminRightsButton = new Button("Manage Admin Rights");
        manageAdminRightsButton.setOnAction(event -> {
            // Redirect to a page where the admin can manage admin rights
            //new ManageAdminRightsPage(stage, helpArticleDatabase, groupName);
        });
        gridPane.add(manageAdminRightsButton, 0, 1);

        // Manage Viewable Users Button
        Button manageViewableButton = new Button("Manage Viewable Users");
        manageViewableButton.setOnAction(event -> {
            // Redirect to a page where the admin can manage viewable users
            //new ManageViewableUsersPage(stage, helpArticleDatabase, groupName);
        });
        gridPane.add(manageViewableButton, 0, 2);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            stage.setScene(Back.back(stage));
        });
        gridPane.add(backButton, 0, 3);

        // Set the scene with the current GridPane
        Scene scene = new Scene(gridPane, 400, 300);
        Back.pushBack(scene);
        stage.setScene(scene);
        stage.show();
    }
}
