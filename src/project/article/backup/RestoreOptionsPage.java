package project.article.backup;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.article.HelpArticleDatabase;
import project.util.Back;
import javafx.stage.Stage;
/**
 * <p> RestoreOptions class </p>
 * 
 * <p> Description: Handles the restore option page using @params to take input and @returning the appropriate results</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
public class RestoreOptionsPage extends VBox {
    private File backupFile = null; // Declaring it up here to prevent Java intricacies

    public RestoreOptionsPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Restore Options");
        
        // Open the operating system's file explorer to find the absolute path of the file
        FileChooser fileNameChooser = new FileChooser();
        fileNameChooser.setTitle("Select Backup File");
        fileNameChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("Text Files", "*.txt")); // only .txt files can be selected
        
        // Button that opens the file chooser
        Button openFileChooserButton = new Button();
        openFileChooserButton.setText("Select Backup File");
        openFileChooserButton.setOnAction(event -> {
        	// if the file doesn't exist, the file will remain null
        	// there are checks in other functions for this (or there should be)
        	backupFile = fileNameChooser.showOpenDialog(stage);
        });

        // Create buttons for restore options
        Button removeExistingButton = new Button("Remove Existing Articles and Restore");
        Button mergeCurrentButton = new Button("Merge with Current Articles");

        // Set up actions for the buttons
        removeExistingButton.setOnAction(event -> {
        	if (backupFile == null) {
        		// If file to open is null, error out and ask to choose another
        		Alert alert = new Alert(Alert.AlertType.ERROR, "Error with restoring file: No file selected", ButtonType.OK);
        		alert.showAndWait();
        	} else {
        		new RemoveExistingAndRestorePage(stage, helpArticleDatabase, backupFile);
        	}
        });

        mergeCurrentButton.setOnAction(event -> {
        	if (backupFile == null) {
        		// If file to open is null, error out and ask to choose another
        		Alert alert = new Alert(Alert.AlertType.ERROR, "Error with restoring file: No file selected", ButtonType.OK);
        		alert.showAndWait();
        	} else {
        		new MergeCurrentRestorePage(stage, helpArticleDatabase, backupFile);
        	}
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        // Add components to the VBox
        getChildren().addAll(new Label("Restore from Backup"), openFileChooserButton, removeExistingButton, mergeCurrentButton, back);

        // Set the scene with the current VBox
        Scene s = new Scene(this, 400, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
