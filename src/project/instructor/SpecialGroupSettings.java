package project.instructor;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.*;
import project.article.HelpArticleDatabase;
import project.util.Back;

/**
 * <p> SpecialGroupSettings Class </p>
 * 
 * <p> Description: The page that allows admin of special access groups to
 *                  edit aspects of the group </p>
 * 
 * @author Group TH 58
 * 
 * @version 1.00        2024-11-20  Inital
 */
public class SpecialGroupSettings extends BorderPane {
    public SpecialGroupSettings(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Special Group Settings");

        Label specialGroupLabel = new Label("Select Group to Edit");
        ComboBox<String> specialGroupSelect = new ComboBox<>();

        try {
            HelpArticleDatabase helpArticleDatabase = new HelpArticleDatabase();
            
            try {
                specialGroupSelect.getItems().addAll(helpArticleDatabase.userGroupsList(user.username));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // MANAGE STUDENTS BUTTON ---------------------------------------------
        Button manageStudentsButton = new Button("Manage Students");
        manageStudentsButton.setOnAction(event -> {
            new ManageSpecialGroupStudents(stage, user, database, specialGroupSelect.getValue());
        });

        // MANAGE GROUPS BUTTON -----------------------------------------------
        Button manageGroupsButton = new Button("Manage Groups");
        manageGroupsButton.setOnAction(event -> {
            new ManageSpecialGroup(stage, user, database, specialGroupSelect.getValue());
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
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(specialGroupLabel, 0, 0);
        centerPane.add(specialGroupSelect, 1, 0, 1, 1);
        centerPane.add(manageStudentsButton, 0, 1);
        centerPane.add(manageGroupsButton, 1, 1);

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

        Scene s = new Scene(this, 300, 300);
        Back.pushBack(s);
		stage.setScene(s);
        stage.show();
    }
}
