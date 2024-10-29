package project;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.util.Arrays;

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

        getChildren().addAll(codeLabel, codeField, submitButton, feedbackLabel);
        stage.setScene(new Scene(this, 300, 200));
        stage.show();
    }
}
