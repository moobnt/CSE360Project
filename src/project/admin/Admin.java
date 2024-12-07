package project.admin;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.account.*;
import project.article.*;
import project.util.Back;
import java.sql.SQLException;

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
            new AdminReset(stage, user, database);
        });
        
        // Delete User Button -------------------------------------------------------------------------------------
        Button deleteButton = new Button("Delete a User");
        deleteButton.setOnAction(event -> {
            new AdminDelete(stage, user, database);
        });
        
        // List Users Button --------------------------------------------------------------------------------------
        Button listUsersButton = new Button("List All Users");
        listUsersButton.setOnAction(event -> {
            new AdminListUsers(stage, user, database);
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
        Back.pushBack(s, "Admin Home Page");
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
