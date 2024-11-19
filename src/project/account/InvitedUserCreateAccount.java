package project.account;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.time.*;

/**
 * <p> InvitedUserCreatAccount class </p>
 * 
 * <p> Description: Uses textfields to build the user invite create account. </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class InvitedUserCreateAccount extends TilePane {

    public InvitedUserCreateAccount(Stage stage, User user, DatabaseModel database, String[] assignedRoles) {
        stage.setTitle("Create Account");

        // Account setup fields
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        TextField confirmPasswordField = new TextField();
        TextField emailField = new TextField();
        TextField firstNameField = new TextField();
        TextField middleNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField preferredNameField = new TextField();

        // Display assigned roles as labels
        Label rolesLabel = new Label("Assigned Roles:");
        Label rolesDisplay = new Label(String.join(",", (CharSequence[]) assignedRoles)); // Display roles as a comma-separated list

        // Create account button
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

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match!");
                return;
            }

            // Assign roles based on the invitation

            // Full name array
            String[] fullName = {firstName, middleName, lastName, preferredName};

            // Register the user in the database
            database.registerUser(username, password, email, assignedRoles, false, OffsetDateTime.now(ZoneOffset.UTC), fullName);

            new LoginService(stage, user, database);
        });

        getChildren().addAll(new Label("Username:"), usernameField,
                             new Label("Email:"), emailField,
                             new Label("Password:"), passwordField,
                             new Label("Confirm Password:"), confirmPasswordField,
                             new Label("First Name:"), firstNameField,
                             new Label("Middle Name:"), middleNameField,
                             new Label("Last Name:"), lastNameField,
                             new Label("Preferred Name (optional):"), preferredNameField,
                             rolesLabel, rolesDisplay, createAccountButton);

        stage.setScene(new Scene(this, 400, 500));
        stage.show();
    }
}
