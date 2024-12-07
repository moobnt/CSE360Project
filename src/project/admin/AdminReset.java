package project.admin;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class AdminReset extends BorderPane {
    public AdminReset(Stage stage, User user, DatabaseModel database) {
        TextField resetUserField = new TextField();
        resetUserField.setPromptText("Enter username to reset");
        
        Label expirationDateLabel = new Label("Set Expiration Date (YYYY-MM-DD):");
        TextField expirationDateField = new TextField(LocalDate.now().plusDays(1).toString()); // Default to next day
        
        Label expirationTimeLabel = new Label("Set Expiration Time (HH:MM):");
        TextField expirationTimeField = new TextField("23:59"); // Default to 11:59 PM

        Label feedbackLabel = new Label();

        Button copyButton = new Button("Copy Code");
        copyButton.setDisable(true);
        
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String username = resetUserField.getText().trim();
            LocalDate expirationDate = LocalDate.parse(expirationDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalTime expirationTime = LocalTime.parse(expirationTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            OffsetDateTime expirationDateTime = OffsetDateTime.of(LocalDateTime.of(expirationDate, expirationTime), ZoneOffset.UTC);
            
            // Generate a one-time code for the reset
            String oneTimeCode = Admin.generateCode();
            
            // Register the reset in the database
            database.resetUserWithCode(username, oneTimeCode, expirationDateTime);
            
            feedbackLabel.setText("User reset successfully. One-time code: " + oneTimeCode);
            
            // Copy button for the one-time code
            copyButton.setDisable(false);
            copyButton.setOnAction(copyEvent -> {
                ClipboardContent content = new ClipboardContent();
                content.putString(oneTimeCode);
                Clipboard.getSystemClipboard().setContent(content);
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
            stage.setScene(Back.back(stage));
        });
        
        // STAGE SETUP --------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(resetUserField, 0, 0, 2, 1);
        centerPane.add(expirationDateLabel, 0, 1);
        centerPane.add(expirationDateField, 1, 1);
        centerPane.add(expirationTimeLabel, 0, 2);
        centerPane.add(expirationTimeField, 1, 2);
        centerPane.add(submitButton, 0, 3, 2, 1);
        centerPane.add(feedbackLabel, 0, 4, 2, 1);
        centerPane.add(copyButton, 0, 5, 2, 1);

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
        Scene s = new Scene(this, 400, 500);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
