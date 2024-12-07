package project.util;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * <p> RoleSelection class </p>
 * 
 * <p> Description: Takes the user input and send them to their appropriate page depending on their role selected(if they have multiple roles)</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.admin.Admin;
import project.instructor.Instructor;
import project.student.Student;

public class RoleSelectionPage extends BorderPane {
    
    public RoleSelectionPage(Stage stage, User user, DatabaseModel database, String[] roles) {
        stage.setTitle("Select Role");
        GridPane centerPane = new GridPane();

        // Create a ToggleGroup to hold radio buttons
        ToggleGroup roleGroup = new ToggleGroup();
        
        // Create radio buttons for each role
        int ii = 0;
        for (String role : roles) {
            // Trim brackets if present
            String trimmedRole = role.trim();
            if (trimmedRole.startsWith("[")) {
                trimmedRole = trimmedRole.substring(1);
            }
            if (trimmedRole.endsWith("]")) {
                trimmedRole = trimmedRole.substring(0, trimmedRole.length() - 1);
            }
            RadioButton radioButton = new RadioButton(trimmedRole); // Use trimmedRole
            radioButton.setToggleGroup(roleGroup);
            centerPane.add(radioButton, ii, 0);
            ii++;
        }

        Button selectButton = new Button("Select Role");
        Label feedbackLabel = new Label();

        selectButton.setOnAction(event -> {
            RadioButton selectedRadioButton = (RadioButton) roleGroup.getSelectedToggle();
            if (selectedRadioButton != null) {
                String selectedRole = selectedRadioButton.getText();
                user.roles = new String[]{selectedRole}; // Set the user's current role
                
                // Redirect to the appropriate home page based on selected role
                if ("Admin".equals(selectedRole)) {
                    new Admin(stage, user, database);
                } else if ("Student".equals(selectedRole)) {
                    new Student(stage, user, database);
                } else if ("Instructor".equals(selectedRole)) {
                    new Instructor(stage, user, database);
                }
            } else {
                feedbackLabel.setText("Please select a role.");
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
            
        // STAGE SETUP --------------------------------------------------------
        
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(selectButton, 0, 1, 3, 1);
        centerPane.add(feedbackLabel, 0, 2, 3, 1);

        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(20));
        bottomPane.setHgap(10);
        bottomPane.setVgap(10);
        bottomPane.add(logOutButton, 0, 0);
        bottomPane.add(quitButton, 1, 0);
        
        this.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.CENTER);
		BorderPane.setMargin(bottomPane, new Insets(20));
        this.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
		BorderPane.setMargin(centerPane, new Insets(20));
        Scene s = new Scene(this, 400, 250);
        stage.setScene(s);
        stage.show();
    }
}
