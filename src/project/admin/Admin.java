package project.admin;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.account.*;
import project.article.*;
import project.util.Back;
import java.util.*;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
* <p> Admin Class </p>
* 
* <p> Description: The GUI handler for all Admin tasks
* 
* @version 1.00 2024-10-09 Initial baseline
*/
public class Admin extends BorderPane {
    
    /**
    * Initializes the Admin home page with options to invite users, reset accounts, and more.
    *
    * @param stage    The window that is currently open, passed by the window that called this page
    * @param user     The current user that is logged in
    * @param database The loaded database instance
    */
    public Admin(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Admin Home Page");
        
        // Invite New User Button --------------------------------------------------------------------------------
        Button inviteButton = new Button("Invite New User");
        inviteButton.setOnAction(event -> {
            new AdminInviteUser(stage, user, database);
        });
        
        // Reset User Button --------------------------------------------------------------------------------------
        Button resetButton = new Button("Reset a User");
        resetButton.setOnAction(event -> {
            TilePane resetLayout = new TilePane();
            Scene resetScene = new Scene(resetLayout, 600, 400);
            
            TextField resetUserField = new TextField();
            resetUserField.setPromptText("Enter username to reset");
            
            Label expirationDateLabel = new Label("Set Expiration Date (YYYY-MM-DD):");
            TextField expirationDateField = new TextField(LocalDate.now().plusDays(1).toString()); // Default to next day
            
            Label expirationTimeLabel = new Label("Set Expiration Time (HH:MM):");
            TextField expirationTimeField = new TextField("23:59"); // Default to 11:59 PM
            
            Button back = new Button("Back");
            back.setOnAction(backEvent -> {
                stage.setScene(Back.back(stage));
            });
            
            Button submitButton = new Button("Submit");
            submitButton.setOnAction(e -> {
                String username = resetUserField.getText().trim();
                LocalDate expirationDate = LocalDate.parse(expirationDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalTime expirationTime = LocalTime.parse(expirationTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                OffsetDateTime expirationDateTime = OffsetDateTime.of(LocalDateTime.of(expirationDate, expirationTime), ZoneOffset.UTC);
                
                // Generate a one-time code for the reset
                String oneTimeCode = generateCode();
                
                // Register the reset in the database
                database.resetUserWithCode(username, oneTimeCode, expirationDateTime);
                
                Text resetSuccessMessage = new Text("User reset successfully. One-time code: " + oneTimeCode);
                
                // Copy button for the one-time code
                Button copyButton = new Button("Copy Code");
                copyButton.setOnAction(copyEvent -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(oneTimeCode);
                    Clipboard.getSystemClipboard().setContent(content);
                });
                
                // Update layout with reset information
                resetLayout.getChildren().clear();
                resetLayout.getChildren().addAll(new Text("User reset successfully."), resetSuccessMessage, copyButton, back);
            });
            
            resetLayout.getChildren().addAll(new Text("Reset User Account"), resetUserField, expirationDateLabel, expirationDateField, 
            expirationTimeLabel, expirationTimeField, submitButton, back);
            Back.pushBack(resetScene);
            stage.setScene(resetScene);
        });
        
        // Delete User Button -------------------------------------------------------------------------------------
        Button deleteButton = new Button("Delete a User");
        deleteButton.setOnAction(event -> {
            TilePane deleteLayout = new TilePane();
            Scene deleteScene = new Scene(deleteLayout, 600, 250);
            
            TextField deleteUserField = new TextField();
            deleteUserField.setPromptText("Enter username to delete");
            
            Button back = new Button("Back");
            back.setOnAction(backEvent -> {
                stage.setScene(Back.back(stage));
            });
            
            Button confirmButton = new Button("Confirm");
            confirmButton.setOnAction(e -> {
                TilePane confirmLayout = new TilePane();
                Scene confirmScene = new Scene(confirmLayout, 600, 250);
                
                String usernameToDelete = deleteUserField.getText().trim();
                if (usernameToDelete.isEmpty()) {
                    deleteLayout.getChildren().addAll(new Text("Username cannot be empty."));
                    return;
                }
                
                Button back2 = new Button("Back");
                back2.setOnAction(backEvent -> {
                    stage.setScene(Back.back(stage));
                });
                
                // Confirmation prompt
                Text confirmText = new Text("Are you sure you want to delete the user '" + usernameToDelete + "'? Type 'Yes' to confirm.");
                TextField confirmationField = new TextField();
                
                Button proceedButton = new Button("Proceed");
                proceedButton.setOnAction(proceedEvent -> {
                    String confirmationInput = confirmationField.getText().trim();
                    if ("Yes".equalsIgnoreCase(confirmationInput)) {
                        database.removeUser(usernameToDelete);
                        confirmLayout.getChildren().clear(); // Clear previous elements
                        confirmLayout.getChildren().addAll(new Text("User deleted successfully."), back2);
                    } else {
                        confirmLayout.getChildren().clear(); // Clear previous elements
                        confirmLayout.getChildren().addAll(new Text("User deletion cancelled."), back2);
                    }
                });
                
                
                confirmLayout.getChildren().clear(); // Clear the previous layout
                confirmLayout.getChildren().addAll(confirmText, confirmationField, proceedButton, back2);
                Back.pushBack(confirmScene);
                stage.setScene(confirmScene);
            });
            
            deleteLayout.getChildren().addAll(new Text("Delete User Account"), deleteUserField, confirmButton, back);
            Back.pushBack(deleteScene);
            stage.setScene(deleteScene);
        });
        
        // List Users Button --------------------------------------------------------------------------------------
        Button listUsersButton = new Button("List All Users");
        listUsersButton.setOnAction(event -> {
            TilePane listLayout = new TilePane();
            Scene listScene = new Scene(listLayout, 600, 250);
            
            Button back = new Button("Back");
            back.setOnAction(backEvent -> {
                stage.setScene(Back.back(stage));
            });
            
            // Fetch and display users using an instance of DatabaseModel
            List<String> users = database.displayUsersByAdmin(); // Call on the instance, not the class
            if (users.isEmpty()) {
                listLayout.getChildren().add(new Text("No Users Registered"));
            } else {
                users.forEach(userInfo -> listLayout.getChildren().add(new Text(userInfo))); // Renamed to userInfo
            }
            
            listLayout.getChildren().add(back);
            Back.pushBack(listScene);
            stage.setScene(listScene);
        });
        
        // Add or Remove Roles Button ------------------------------------------------------------------------------
        Button addOrRemoveUserButton = new Button("Add or Remove Roles");
        addOrRemoveUserButton.setOnAction(event -> {
            new AdminEditRoles(stage, user, database);
        });
        
        // Manage Help Articles Button ----------------------------------------------------------------------------
        Button helpArticleManagementButton = new Button("Manage Help Articles");
        helpArticleManagementButton.setOnAction(event -> {
            HelpArticleDatabase h = null;
            try {
                h = new HelpArticleDatabase();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HelpArticleManagementPage(stage, h, 1);
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
        
        // Show buttons in the scene ------------------------------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(inviteButton, 0, 0);
        centerPane.add(resetButton, 1, 0);
        centerPane.add(deleteButton, 2, 0);
        centerPane.add(listUsersButton, 0, 1);
        centerPane.add(addOrRemoveUserButton, 1, 1);
        centerPane.add(helpArticleManagementButton, 2, 1);
        
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
        Scene s = new Scene(this, 500, 250);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
    
    /**
    * Generates a random string of lowercase letters for the one-time passcode.
    *
    * @return the generated string
    */
    protected static String generateCode() {
        // Generate a random 8-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            code.append(chars.charAt(randomIndex));
        }
        return code.toString();
    }
}
