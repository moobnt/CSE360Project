package project;

import java.nio.charset.Charset;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Admin extends TilePane {
	
	public String codeGen() {
		byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));

	    return generatedString;
	}
	
	public Admin(Stage stage, User user, DatabaseModel database) {
		stage.setTitle("Admin Home Page");
		
		// Button set up
		Button invite = new Button();
		invite.setText("Invite New User");
		invite.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane inviteLayout = new TilePane();
				Scene inviteScene = new Scene(inviteLayout, 600, 250);
				
				// Scene elements
				Button newInvite = new Button();
				newInvite.setText("Generate a new invite.");
				CheckBox instructorRole = new CheckBox();
				instructorRole.setText("Instructor");
				newInvite.setOnAction(new EventHandler<>() {
					public void handle(ActionEvent event) {
						String inviteCode = codeGen();
					}
				});
				
				stage.setScene(inviteScene);
			}
		});
		
		Button reset = new Button();
		reset.setText("Reset a User");
		reset.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				TilePane resetLayout = new TilePane();
				Scene resetScene = new Scene(resetLayout, 600, 250);
				
				
				
				stage.setScene(resetScene);
			}
		});
		
		
		Button delete = new Button();
		delete.setText("Delete a User");
		delete.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				TilePane deleteLayout = new TilePane();
				Scene deleteScene = new Scene(deleteLayout, 600, 250);
				stage.setScene(deleteScene);
			}
		});
		
		
		Button list = new Button();
		list.setText("List All Users");
		list.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				TilePane listLayout = new TilePane();
				Scene listScene = new Scene(listLayout, 600, 250);
				stage.setScene(listScene);
			}
		});
		
		
		Button addOrRemove = new Button();
		addOrRemove.setText("Add or Remove Users");
		addOrRemove.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				TilePane addOrRemoveLayout = new TilePane();
				Scene addOrRemoveScene = new Scene(addOrRemoveLayout, 600, 250);
				stage.setScene(addOrRemoveScene);
			}
		});
		
		
		Button logOut = new Button();
		logOut.setText("Log Out");
		logOut.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				LoginService login = new LoginService(stage, user, database);
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
		
		getChildren().add(buttonBox);
		stage.setScene(new Scene(this, 600, 250));
		stage.show();
		
	}
	
	
}