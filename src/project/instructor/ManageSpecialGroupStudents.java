package project.instructor;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

/**
 * <p> ManageSpecialGroupStudents Class </p>
 * 
 * <p> Description: The page that allows instructors in a group to edit students in the group </p>
 * 
 * @author Group TH 58
 * 
 * @version 1.00        2024-11-20  Initial
 */
public class ManageSpecialGroupStudents extends BorderPane {
    public ManageSpecialGroupStudents(Stage stage, User user, DatabaseModel database, String group) {
        stage.setTitle("Manage Students");

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
        back.setOnAction(event -> {
        	Back.back(stage);
        	
        });
        
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

        Scene s = new Scene(this, 300, 300);
        Back.pushBack(s, "Manage Students");
		stage.setScene(s);
        stage.show();

    }
}
