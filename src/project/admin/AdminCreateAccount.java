package project.admin;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * <p> Admin create account </p>
 * 
 * <p> Description: Account creation handler used to create Admin account</p>
 * 
 * @version 1.00 2024-10-39 Initial baseline
 */


public class AdminCreateAccount extends TilePane {

    public AdminCreateAccount(Stage stage, DatabaseModel database, String initialUsername, String initialPassword) {
        stage.setTitle("Create Admin Account");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField(initialUsername);
        
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField(initialPassword);
        
        Label confirmPasswordLabel = new Label("Confirm Password:");
        TextField confirmPasswordField = new TextField();
        
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        
        Label middleNameLabel = new Label("Middle Name:");
        TextField middleNameField = new TextField();
        
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        
        Label preferredNameLabel = new Label("Preferred Name (Optional):");
        TextField preferredNameField = new TextField();

        // Roles Checkboxes
        CheckBox adminCheckBox = new CheckBox("Admin");
        adminCheckBox.setSelected(true);
        adminCheckBox.setDisable(true);
        
        CheckBox studentCheckBox = new CheckBox("Student");
        CheckBox instructorCheckBox = new CheckBox("Instructor");

        Button createAccountButton = new Button("Create Account");

        createAccountButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();
            String email = emailField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String middleName = middleNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String preferredName = preferredNameField.getText().trim();

            // Check if password and confirm password match
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match!");
                return;
            }

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
                // Collect roles
                java.util.List<String> rolesList = new java.util.ArrayList<>();
                rolesList.add("Admin");  // Admin is required for the first user
                if (studentCheckBox.isSelected()) rolesList.add("Student");
                if (instructorCheckBox.isSelected()) rolesList.add("Instructor");

                String[] roles = rolesList.toArray(new String[0]);
                String[] fullName = {firstName, middleName, lastName, preferredName}; 
                OffsetDateTime currentDate = OffsetDateTime.now(ZoneOffset.UTC);

                database.registerUser(username, password, email, roles, false, currentDate, fullName);

                // Redirect to Admin home page after successful account creation
                User adminUser = new User(username, password, roles);
                new Admin(stage, adminUser, database);
            } else {
                System.out.println("All fields must be filled except Preferred Name.");
            }
        });

        getChildren().addAll(usernameLabel, usernameField, 
                             passwordLabel, passwordField, 
                             confirmPasswordLabel, confirmPasswordField,  // New confirm password field
                             emailLabel, emailField,
                             firstNameLabel, firstNameField, 
                             middleNameLabel, middleNameField,
                             lastNameLabel, lastNameField, 
                             preferredNameLabel, preferredNameField,
                             adminCheckBox, studentCheckBox, instructorCheckBox, 
                             createAccountButton);

        stage.setScene(new Scene(this, 400, 550));
        stage.show();
    }
}
