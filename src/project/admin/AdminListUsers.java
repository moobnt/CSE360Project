package project.admin;

import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class AdminListUsers extends BorderPane {
    public AdminListUsers(Stage stage, User user, DatabaseModel database) {
        Label feedbackLabel = new Label();
        ScrollPane scrollPane = new ScrollPane();
        stage.setTitle("List Users");
        VBox usersBox = new VBox();
        // Fetch and display users using an instance of DatabaseModel
        List<String> users = database.displayUsersByAdmin(); // Call on the instance, not the class
        if (users.isEmpty()) {
            feedbackLabel.setText("No users registered.");
        } else {
            users.forEach(userInfo -> usersBox.getChildren().addAll(new Text(userInfo), new Text())); // Renamed to userInfo
        }

        scrollPane.setContent(usersBox);
        
                
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
        centerPane.add(scrollPane, 0, 0);
        centerPane.add(feedbackLabel, 0, 1);

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
        Scene s = new Scene(this, 500, 500);
        Back.pushBack(s, "List Users");
        stage.setScene(s);
        stage.show();
    }
}
