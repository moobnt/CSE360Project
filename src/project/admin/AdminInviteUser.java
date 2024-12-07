package project.admin;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class AdminInviteUser extends BorderPane {
    public AdminInviteUser(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Invite User");
        
        // Checkboxes for selecting roles
        CheckBox instructorRole = new CheckBox("Instructor");
        CheckBox studentRole = new CheckBox("Student");
        CheckBox adminRole = new CheckBox("Admin");
        
        Button generateInviteButton = new Button("Generate Invite Code");
        
        Label feedbackLabel = new Label();
        
        Button copyButton = new Button("Copy Code");
        copyButton.setDisable(true);
        
        generateInviteButton.setOnAction(e -> {
            // Generate a unique invite code
            String inviteCode = Admin.generateCode();
            
            java.util.List<String> rolesList = new java.util.ArrayList<>(); // TODO: THIS **stuff** DONT WORK RIGHT
            
            // Collect selected roles
            if (instructorRole.isSelected()) rolesList.add("Instructor");
            if (studentRole.isSelected()) rolesList.add("Student");
            if (adminRole.isSelected()) rolesList.add("Admin");
            
            String[] roles = rolesList.toArray(new String[0]);
            
            if (roles != null) {
                // Register the invite code with associated roles
                database.registerCode(inviteCode, roles);
                
                // Display the invite code and add a copy button
                feedbackLabel.setText("Invite code created: " + inviteCode);
                copyButton.setDisable(false);
                copyButton.setOnAction(copyEvent -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(inviteCode);
                    Clipboard.getSystemClipboard().setContent(content);
                });
                
                // Update the invite layout to show the code and options
                
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
        
        // BACK ---------------------------------------------------------------
        Button back = new Button("Back");
        back.setOnAction(backEvent -> {
            stage.setScene(Back.back(stage));
        });
        
        // STAGE SETUP --------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(adminRole, 0, 0);
        centerPane.add(instructorRole, 1, 0);
        centerPane.add(studentRole, 2, 0);
        centerPane.add(generateInviteButton, 0, 1, 3, 1);
        centerPane.add(feedbackLabel, 0, 2, 3, 1);
        centerPane.add(copyButton, 0, 3, 3, 1);
        
        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(20));
        bottomPane.setHgap(10);
        bottomPane.setVgap(10);
        bottomPane.add(back, 0, 0);
        bottomPane.add(logOutButton, 1, 0);
        bottomPane.add(quitButton, 2, 0);
        
        this.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.CENTER);
        BorderPane.setMargin(bottomPane, new Insets(20));
        this.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
        BorderPane.setMargin(centerPane, new Insets(20));
        Scene s = new Scene(this, 400, 300);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
