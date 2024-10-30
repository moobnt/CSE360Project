package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RestoreOptionsPage extends VBox {
    private HelpArticleDatabase helpArticleDatabase;

    public RestoreOptionsPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        this.helpArticleDatabase = helpArticleDatabase; // Store the database instance
        stage.setTitle("Restore Options");

        // Create a TextField for the filename
        TextField filenameField = new TextField();
        filenameField.setPromptText("Enter the backup filename");

        // Create buttons for restore options
        Button removeExistingButton = new Button("Remove Existing Articles and Restore");
        Button mergeCurrentButton = new Button("Merge with Current Articles");

        // Set up actions for the buttons
        removeExistingButton.setOnAction(event -> {
            String filename = filenameField.getText();
            RemoveExistingAndRestorePage removePage = new RemoveExistingAndRestorePage(stage, helpArticleDatabase, filename);
        });

        mergeCurrentButton.setOnAction(event -> {
            String filename = filenameField.getText();
            MergeCurrentRestorePage mergePage = new MergeCurrentRestorePage(stage, helpArticleDatabase, filename);
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Restore from Backup"), filenameField, removeExistingButton, mergeCurrentButton);

        // Set the scene with the current VBox
        stage.setScene(new Scene(this, 400, 200));
        stage.show();
    }
}
