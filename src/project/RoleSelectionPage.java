package project;

import javafx.scene.Scene;

/**
 * <p> RoleSelection class </p>
 * 
 * <p> Description: Takes the user input and send them to their appropriate page depending on their role selected(if they have multiple roles)</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.student.Student;

public class RoleSelectionPage extends TilePane {
    
    public RoleSelectionPage(Stage stage, User user, DatabaseModel database, String[] roles) {
        stage.setTitle("Select Role");

        // Create a ToggleGroup to hold radio buttons
        ToggleGroup roleGroup = new ToggleGroup();
        
        // Create radio buttons for each role
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
            getChildren().add(radioButton);
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

        getChildren().addAll(new Label("Select your role for this session:"), selectButton, feedbackLabel);
        stage.setScene(new Scene(this, 300, 200));
        stage.show();
    }
}
