package project.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class AdminEditRoles extends BorderPane {
    public AdminEditRoles(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Edit User Roles");
        
        TextField userField = new TextField();
        userField.setPromptText("Enter username");

        CheckBox instructorRole = new CheckBox("Instructor");
        CheckBox studentRole = new CheckBox("Student");
        CheckBox adminRole = new CheckBox("Admin");

        Label feedbackLabel = new Label(); // Label to display feedback messages

        // Button to Add Roles
        Button addRolesButton = new Button("Add Roles");
        addRolesButton.setOnAction(e -> {
            String username = userField.getText().trim();
            List<String> roleList = new ArrayList<>();

            // Collect selected roles
            if (instructorRole.isSelected()) roleList.add("Instructor");
            if (studentRole.isSelected()) roleList.add("Student");
            if (adminRole.isSelected()) roleList.add("Admin");

            if (!username.isEmpty() && !roleList.isEmpty()) {
                if (database.doesUserExist(username)) {
                    // Retrieve current roles of the user from the database
                    String[] currentRoles = database.getUserRoles(username);
                    List<String> currentRoleList = new ArrayList<>(Arrays.asList(currentRoles));

                    for (String role : roleList) {
                        if (!currentRoleList.contains(role)) {
                            // If the role doesn't exist, add it
                            currentRoleList.add(role);
                        }
                    }

                    // Update user roles in the database
                    database.updateUserRoles(username, currentRoleList.toArray(new String[0]));
                    feedbackLabel.setText("Roles added successfully."); // Update feedback label
                } else {
                    feedbackLabel.setText("User does not exist."); // Update feedback label
                }
            } else {
                feedbackLabel.setText("Please enter a username and select at least one role."); // Update feedback label
            }
        });

        // Button to Remove Roles
        Button removeRolesButton = new Button("Remove Roles");
        removeRolesButton.setOnAction(e -> {
            String username = userField.getText().trim();
            List<String> roleList = new ArrayList<>();

            // Collect selected roles
            if (instructorRole.isSelected()) roleList.add("Instructor");
            if (studentRole.isSelected()) roleList.add("Student");
            if (adminRole.isSelected()) roleList.add("Admin");

            if (!username.isEmpty() && !roleList.isEmpty()) {
                if (database.doesUserExist(username)) {
                    // Retrieve current roles of the user from the database
                    String[] currentRoles = database.getUserRoles(username);
                    List<String> currentRoleList = new ArrayList<>(Arrays.asList(currentRoles));

                    for (String role : roleList) {
                        currentRoleList.remove(role); // Remove selected roles
                    }

                    // Update user roles in the database
                    database.updateUserRoles(username, currentRoleList.toArray(new String[0]));
                    feedbackLabel.setText("Roles removed successfully."); // Update feedback label
                } else {
                    feedbackLabel.setText("User does not exist."); // Update feedback label
                }
            } else {
                feedbackLabel.setText("Please enter a username and select at least one role."); // Update feedback label
            }
        });

        // LOG OUT ------------------------------------------------------------
        Button logOutButton = new Button("Log out");
        logOutButton.setOnAction(event -> {   		
        		//send back to login page
        		try {
					new LoginService(stage, new User(), database);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            });
        
        // QUIT ---------------------------------------------------------------
        Button quitButton = new Button("Quit");
		quitButton.setOnAction(event -> {
			Alert quitAlert = new Alert(AlertType.CONFIRMATION, 
										"Are you sure you want to quit?", 
										ButtonType.YES, ButtonType.NO);
			quitAlert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					System.exit(0);
				}
			});
		});

        // BACK ---------------------------------------------------------------
        Button back = new Button("Back");
        back.setOnAction(backEvent -> {
            Back.back(stage);
        });

        // STAGE SETUP --------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(userField, 0, 0, 3, 1);
        centerPane.add(adminRole, 0, 1);
        centerPane.add(instructorRole, 1, 1);
        centerPane.add(studentRole, 2, 1);
        centerPane.add(feedbackLabel, 0, 2, 3, 1);
        centerPane.add(addRolesButton, 0, 3);
        centerPane.add(removeRolesButton, 1, 3);

        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(20));
        bottomPane.setHgap(10);
        bottomPane.setVgap(10);
        bottomPane.add(back, 0, 0);
        bottomPane.add(logOutButton, 1, 0);
        bottomPane.add(quitButton, 2, 0);

        this.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.CENTER);
		BorderPane.setMargin(bottomPane, new Insets(20));
        this.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
		BorderPane.setMargin(centerPane, new Insets(20));
        Scene s = new Scene(this, 400, 300);
        Back.pushBack(s, "Edit User Roles");
        stage.setScene(s);
        stage.show();
    }
}
