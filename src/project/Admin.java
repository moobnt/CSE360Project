package project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * <p> Admin Class </p>
 * 
 * <p> Description: The GUI handler for all Admin tasks
 * 
 * @version 1.00 2024-10-09 Initial baseline
 */
public class Admin extends TilePane {

    /**
     * Generates a random string of lowercase letters for the one-time passcode.
     *
     * @return the generated string
     */
    public String generateInviteCode() {
        int leftLimit = 97; // 'a'
        int rightLimit = 122; // 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
    
    private String generateOneTimeCode() {
        // Generate a random 8-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            code.append(chars.charAt(randomIndex));
        }
        return code.toString();
    }

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
            TilePane inviteLayout = new TilePane();
            Scene inviteScene = new Scene(inviteLayout, 600, 300);

            // Checkboxes for selecting roles
            CheckBox instructorRole = new CheckBox("Instructor");
            CheckBox studentRole = new CheckBox("Student");
            CheckBox adminRole = new CheckBox("Admin");

            Button generateInviteButton = new Button("Generate Invite Code");

            generateInviteButton.setOnAction(e -> {
                // Generate a unique invite code
                String inviteCode = generateInviteCode();
                List<String> roles = new ArrayList<>();
                
                // Collect selected roles
                if (instructorRole.isSelected()) roles.add("Instructor");
                if (studentRole.isSelected()) roles.add("Student");
                if (adminRole.isSelected()) roles.add("Admin");

                if (!roles.isEmpty()) {
                    // Register the invite code with associated roles
                    database.registerCode(inviteCode, roles.toArray(String[]::new));

                    // Display the invite code and add a copy button
                    Text inviteCodeDisplay = new Text("Invite code created: " + inviteCode);
                    Button copyButton = new Button("Copy Code");
                    copyButton.setOnAction(copyEvent -> {
                        ClipboardContent content = new ClipboardContent();
                        content.putString(inviteCode);
                        Clipboard.getSystemClipboard().setContent(content);
                    });

                    // Update the invite layout to show the code and options
                    inviteLayout.getChildren().clear();
                    inviteLayout.getChildren().addAll(inviteCodeDisplay, copyButton, instructorRole, studentRole, adminRole, generateInviteButton);
                } else {
                    System.out.println("Please select at least one role before generating the invite code.");
                }
            });

            inviteLayout.getChildren().addAll(instructorRole, studentRole, adminRole, generateInviteButton);
            stage.setScene(inviteScene);
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

            Button submitButton = new Button("Submit");
            submitButton.setOnAction(e -> {
                String username = resetUserField.getText().trim();
                LocalDate expirationDate = LocalDate.parse(expirationDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalTime expirationTime = LocalTime.parse(expirationTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                OffsetDateTime expirationDateTime = OffsetDateTime.of(LocalDateTime.of(expirationDate, expirationTime), ZoneOffset.UTC);

                // Generate a one-time code for the reset
                String oneTimeCode = generateOneTimeCode();

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
                resetLayout.getChildren().addAll(new Text("User reset successfully."), resetSuccessMessage, copyButton);
            });

            resetLayout.getChildren().addAll(new Text("Reset User Account"), resetUserField, expirationDateLabel, expirationDateField, 
                                             expirationTimeLabel, expirationTimeField, submitButton);
            stage.setScene(resetScene);
        });
        
     // Delete User Button -------------------------------------------------------------------------------------
        Button deleteButton = new Button("Delete a User");
        deleteButton.setOnAction(event -> {
            TilePane deleteLayout = new TilePane();
            Scene deleteScene = new Scene(deleteLayout, 600, 250);

            TextField deleteUserField = new TextField();
            deleteUserField.setPromptText("Enter username to delete");

            Button confirmButton = new Button("Confirm");
            confirmButton.setOnAction(e -> {
                String usernameToDelete = deleteUserField.getText().trim();
                if (usernameToDelete.isEmpty()) {
                    deleteLayout.getChildren().add(new Text("Username cannot be empty."));
                    return;
                }
                
                // Confirmation prompt
                Text confirmText = new Text("Are you sure you want to delete the user '" + usernameToDelete + "'? Type 'Yes' to confirm.");
                TextField confirmationField = new TextField();
                
                Button proceedButton = new Button("Proceed");
                proceedButton.setOnAction(proceedEvent -> {
                    String confirmationInput = confirmationField.getText().trim();
                    if ("Yes".equalsIgnoreCase(confirmationInput)) {
                        database.removeUser(usernameToDelete);
                        deleteLayout.getChildren().clear(); // Clear previous elements
                        deleteLayout.getChildren().add(new Text("User deleted successfully."));
                    } else {
                        deleteLayout.getChildren().clear(); // Clear previous elements
                        deleteLayout.getChildren().add(new Text("User deletion cancelled."));
                    }
                });
                
                deleteLayout.getChildren().clear(); // Clear the previous layout
                deleteLayout.getChildren().addAll(confirmText, confirmationField, proceedButton);
            });

            deleteLayout.getChildren().addAll(new Text("Delete User Account"), deleteUserField, confirmButton);
            stage.setScene(deleteScene);
        });

     // List Users Button --------------------------------------------------------------------------------------
        Button listUsersButton = new Button("List All Users");
        listUsersButton.setOnAction(event -> {
            TilePane listLayout = new TilePane();
            Scene listScene = new Scene(listLayout, 600, 250);

            // Fetch and display users using an instance of DatabaseModel
            List<String> users = database.displayUsersByAdmin(); // Call on the instance, not the class
            if (users.isEmpty()) {
                listLayout.getChildren().add(new Text("No Users Registered"));
            } else {
                users.forEach(userInfo -> listLayout.getChildren().add(new Text(userInfo))); // Renamed to userInfo
            }

            stage.setScene(listScene);
        });

        // Add or Remove User Button ------------------------------------------------------------------------------
        Button addOrRemoveUserButton = new Button("Add or Remove Roles");
        addOrRemoveUserButton.setOnAction(event -> {
            TilePane addOrRemoveLayout = new TilePane();
            Scene addOrRemoveScene = new Scene(addOrRemoveLayout, 600, 300);

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

            addOrRemoveLayout.getChildren().addAll(
                    new Label("Modify Roles for User"), userField,
                    instructorRole, studentRole, adminRole,
                    addRolesButton, removeRolesButton, feedbackLabel // Add the feedback label here
            );
            stage.setScene(addOrRemoveScene);
        });
        
        // CreateHelpArticlePage Button ------------------------------------------------------------------------------------------
        Button createHelpArticleButton = new Button("Creeate Help Articles");
        createHelpArticleButton.setOnAction(event -> {
        	HelpArticleDatabase helpArticleDatabase = new HelpArticleDatabase(); // Create an instance of HelpArticleDatabase
        	CreateHelpArticlePage createHelpArticlePage = new CreateHelpArticlePage(stage, helpArticleDatabase);
        });
        
        // HelpArticlesManagementPage Button ------------------------------------------------------------------------------------------
        Button updateHelpArticlesButton = new Button("Update Help Articles");
        updateHelpArticlesButton.setOnAction(event -> {
        	HelpArticleDatabase helpArticleDatabase = new HelpArticleDatabase(); // Create an instance of HelpArticleDatabase
            UpdateHelpArticlePage updateHelpArticlePage = new UpdateHelpArticlePage(stage, helpArticleDatabase);
        });

        // Logout Button ------------------------------------------------------------------------------------------
        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(event -> new LoginService(stage, user, database));

        // Arrange buttons horizontally ---------------------------------------------------------------------------
        HBox buttonBox = new HBox(10, inviteButton, resetButton, deleteButton, listUsersButton, addOrRemoveUserButton, createHelpArticleButton, updateHelpArticlesButton, logoutButton);

        // Show buttons in the scene ------------------------------------------------------------------------------
        getChildren().add(buttonBox);
        stage.setScene(new Scene(this, 600, 250));
        stage.show();
    }
}
