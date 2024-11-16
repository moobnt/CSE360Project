package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.util.Back;

/**
 * <p> Invitation Code Page class </p>
 * 
 * <p> Description: Handles the page for the invitation codes, takes in the user code as a @param then provides
 * the appropriate @return value </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class InvitationCodePage extends TilePane {

    public InvitationCodePage(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Use Invitation Code");

        Label codeLabel = new Label("Enter Invitation Code:");
        TextField codeField = new TextField();
        Button submitButton = new Button("Submit Code");
        Label feedbackLabel = new Label();

        submitButton.setOnAction(event -> {
            String code = codeField.getText().trim();

            // Validate the invitation code
            if (database.hasCode(code)) {
                // Retrieve roles associated with the code
                String[] assignedRoles = database.getCodeRoles(code);
                if (assignedRoles != null && assignedRoles.length > 0) {
                    // Pass roles to InvitedUserCreateAccount for role-specific setup
                    new InvitedUserCreateAccount(stage, user, database, assignedRoles);
                } else {
                    feedbackLabel.setText("No valid roles assigned to this code.");
                }
            } else {
                feedbackLabel.setText("Invalid or expired invitation code.");
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        getChildren().addAll(codeLabel, codeField, submitButton, feedbackLabel, back);

        Scene s = new Scene(this, 300, 200);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
