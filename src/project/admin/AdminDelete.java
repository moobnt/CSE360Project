package project.admin;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class AdminDelete extends BorderPane {
    public AdminDelete(Stage stage, User user, DatabaseModel database) {      
        TextField deleteUserField = new TextField();
        deleteUserField.setPromptText("Enter username to delete");
        stage.setTitle("Delete User");

        Label feedbackLabel = new Label();
        
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {         
            String usernameToDelete = deleteUserField.getText().trim();
            if (usernameToDelete.isEmpty()) {
                feedbackLabel.setText("Please enter a user to delete.");
                return;
            }
            
            // Confirmation prompt
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION, 
                "Are you sure you want to delete " + usernameToDelete + "?",
                ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    database.removeUser(usernameToDelete);
                    feedbackLabel.setText("User deleted successfully.");
                } else {
                    feedbackLabel.setText("User deletion cancelled.");
                }
            });
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
            Back.back(stage);
        });
        
        // STAGE SETUP --------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(deleteUserField, 0, 0, 2, 1);
        centerPane.add(confirmButton, 3, 0);
        centerPane.add(feedbackLabel, 0, 1, 3, 1);

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
        Scene s = new Scene(this, 500, 250);
        Back.pushBack(s, "Delete User");
        stage.setScene(s);
        stage.show();
    }
}
