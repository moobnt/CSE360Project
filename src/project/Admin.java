package project;

import java.sql.SQLException;
import java.util.*;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * <p> Admin Class </p>
 * 
 * <p> Description: The GUI handler for all Admin tasks
 * 
 * @author Group TH 58
 * 
 * @version 1.00	2024-10-09 Initial baseline
 */

public class Admin extends TilePane {
	
	/**
	 * This method creates a string of random lowercase letters
	 * This is used to create the one-time passcode
	 * 
	 * @return Returns the generated string
	 */
	public String codeGen() {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    return generatedString;
	}
	
	
	/**
	 * This function is responsible for calling the GUI and handling user data and the database
	 * This will begin at a home page and then bring the user to different pages based on
	 * the desired function
	 * 
	 * @param stage The window that is currently open, passed by the window that called this page
	 * @param user The current user that is logged in
	 * @param database The database that is loaded
	 */
	public Admin(Stage stage, User user, DatabaseModel database) {
		stage.setTitle("Admin Home Page");
		
		// Invite button -------------------------------------------------------------------------------------------
		// Initialize button
		Button invite = new Button();
		invite.setText("Invite New User");
		invite.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane inviteLayout = new TilePane();
				Scene inviteScene = new Scene(inviteLayout, 600, 250);
				
				// Scene elements
				// Buttons ---
				Button newInvite = new Button();
				newInvite.setText("Generate a new invite.");
				
				//Checkboxes ---
				CheckBox instructorRole = new CheckBox();
				instructorRole.setText("Instructor");
				CheckBox studentRole = new CheckBox();
				studentRole.setText("Student");
				CheckBox adminRole = new CheckBox();
				adminRole.setText("Admin");
				
				// Check what roles are requested and make a string
				// containing them to pass on to registerCode
				// Doing this w/ an array list so that adding the strings to the array
				// doesn't have complex looking logic
				List<String> roleList = new ArrayList<>();
				
				// All if statements so that we can account for multiple roles
				if (instructorRole.isSelected()) {
					roleList.add("instructor");
				} if (studentRole.isSelected()) {
					roleList.add("student");
				} if (adminRole.isSelected()) {
					roleList.add("admin");
				}
				
				// Once the generate button is pressed, the admin is brought to a new page
				// where the code is displayed to give to the new user
				newInvite.setOnAction(new EventHandler<>() {
					public void handle(ActionEvent event) {
						// Generate a random code once the button is pressed
						String inviteCode = codeGen();
						
						// Scene initialization
						TilePane inviteCodeLayout = new TilePane();
						Scene inviteCodeScene = new Scene(inviteCodeLayout, 600, 250);
						
						// Text box for invite code
						Text inviteCodeDisplay = new Text();
						inviteCodeDisplay.setText("Invite code created: " + inviteCode);
						
						database.registerCode(inviteCode, roleList.toArray(String[]::new));
						
						// Adds all elements and shows the scene
						inviteCodeLayout.getChildren().addAll(inviteCodeDisplay);
						stage.setScene(inviteCodeScene);
					}
				});
				
				// Adds all elements and shows the scene
				inviteLayout.getChildren().addAll(newInvite, instructorRole, studentRole, adminRole);
				stage.setScene(inviteScene);
			}
		});
		
		// Reset button --------------------------------------------------------------------------------------------
		// Initialize button
		Button reset = new Button();
		reset.setText("Reset a User");
		reset.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane resetLayout = new TilePane();
				Scene resetScene = new Scene(resetLayout, 600, 250);
				
				// TODO: Reset user button
				
				stage.setScene(resetScene);
			}
		});
		
		// Delete button -------------------------------------------------------------------------------------------
		// Initialize button
		Button delete = new Button();
		delete.setText("Delete a User");
		delete.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane deleteLayout = new TilePane();
				Scene deleteScene = new Scene(deleteLayout, 600, 250);
				
				// Text input box ---
				TextField deleteUser = new TextField();
				deleteUser.setText("Username");
				
				// Buttons ---
				Button confirm = new Button();
				confirm.setText("Confirm");
				confirm.setOnAction(e -> {
					database.removeUser(deleteUser.getText());
				});
				
				// Adds all elements and shows the scene
				deleteLayout.getChildren().addAll(deleteUser, confirm);
				stage.setScene(deleteScene);
			}
		});
		
		// User list button ----------------------------------------------------------------------------------------
		// Initialize button
		Button list = new Button();
		list.setText("List All Users");
		list.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane listLayout = new TilePane();
				Scene listScene = new Scene(listLayout, 600, 250);
				
				// TODO: List all users button
				
				// Create a list of all users and their data and print it out line by line
				// Format: USERNAME: LAST, FIRST MIDDLE | ROLE1, ROLE2, ROLE3
				
				try {
					DatabaseModel.displayUsersbyAdmin();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stage.setScene(listScene);
			}
		});
		
		// Add or remove user button -------------------------------------------------------------------------------
		// Initialize button
		Button addOrRemove = new Button();
		addOrRemove.setText("Add or Remove Users");
		addOrRemove.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Scene initialization
				TilePane addOrRemoveLayout = new TilePane();
				Scene addOrRemoveScene = new Scene(addOrRemoveLayout, 600, 250);
				
				// TODO: Add or remove users button
				
				stage.setScene(addOrRemoveScene);
			}
		});
		
		// Logout button -------------------------------------------------------------------------------------------
		// Initialize button
		Button logOut = new Button();
		logOut.setText("Log Out");
		logOut.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// Simply calls the login page which will overwrite the user
				LoginService login = new LoginService(stage, user, database);
			}
		});
		
		// Horizontal box to hold the buttons
		HBox buttonBox = new HBox(10, invite, reset, delete, list, addOrRemove, logOut);
		
		// Adding all buttons to the scene and then showing the scene
		getChildren().add(buttonBox);
		stage.setScene(new Scene(this, 600, 250)); // Resolution of 600x250 to show the buttons properly
		stage.show();
		
	}
	
	
}