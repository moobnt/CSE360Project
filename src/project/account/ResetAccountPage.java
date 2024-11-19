package project.account;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.util.Back;
/**
 * <p> ResetAccountPage class </p>
 * 
 * <p> Description: Resets the users account, using texfield and tilepane to take inputs and outputs the reset passwords</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
public class ResetAccountPage extends TilePane {

    public ResetAccountPage(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Reset Account");

        // Fields for one-time code and new password
        Label usernameLabel = new Label("Enter username:");
        TextField usernameField = new TextField();
        Label codeLabel = new Label("Enter One-Time Code:");
        TextField codeField = new TextField();
        Label newPasswordLabel = new Label("Enter New Password:");
        PasswordField newPasswordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm New Password:");
        PasswordField confirmPasswordField = new PasswordField();
        
        Button resetPasswordButton = new Button("Reset Password");
        Label feedbackLabel = new Label();

        resetPasswordButton.setOnAction(event -> {
        	String username = usernameField.getText().trim();
            String enteredCode = codeField.getText().trim();
            String newPassword = newPasswordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (!newPassword.equals(confirmPassword)) {
                feedbackLabel.setText("Passwords do not match!");
                return;
            }

            // Validate one-time code and expiration time
            if (database.validateOneTimeCode(username, enteredCode)) {
                // Reset the password
                database.updatePassword(username, newPassword);
                database.clearOneTimeCode(username); // Clear one-time code and reset flag
                database.editUser(username, "isReset", false); // Set isReset flag back to false
                feedbackLabel.setText("Password reset successful. Please log in.");

                // Redirect back to the login page after reset
                new LoginService(stage, user, database);
            } else {
                feedbackLabel.setText("Invalid or expired one-time code.");
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        getChildren().addAll(usernameLabel, usernameField, codeLabel, codeField, newPasswordLabel, newPasswordField, confirmPasswordLabel, confirmPasswordField, resetPasswordButton, feedbackLabel, back);

        Scene s = new Scene(this, 400, 300);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
