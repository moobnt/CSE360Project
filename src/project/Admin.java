package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Admin extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage ) {
		primaryStage.setTitle("Admin Home Page");
		
		// Button set up
		Button invite = new Button();
		invite.setText("Invite New User");
		
		Button reset = new Button();
		reset.setText("Reset");
		
		Button delete = new Button();
		delete.setText("Delete");
		
		Button list = new Button();
		list.setText("List All Users");
		
		Button addOrRemove = new Button();
		addOrRemove.setText("Add or Remove Users");
		
		Button logOut = new Button();
		logOut.setText("Log Out");
		logOut.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Get the current window
				Stage stage = (Stage) logOut.getScene().getWindow();
				stage.setScene(new Scene(new Pane()));
			}
		});
		
		// Horizontal box to hold the buttons
		HBox buttonBox = new HBox(10, invite, reset, delete, list, addOrRemove, logOut);
		
		// Requirements:
		//   Invite to join
		//   Reset a user
		//   Delete a user
		//   List all users
		//   Add / remove user role
		//   Log out
		
		StackPane root = new StackPane();
		root.getChildren().add(buttonBox);
		primaryStage.setScene(new Scene(root, 600, 250));
		primaryStage.show();
		
	}
}