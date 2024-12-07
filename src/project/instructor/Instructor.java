package project.instructor;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.article.ArticleHome;
import project.article.HelpArticleDatabase;
import project.article.HelpArticleManagementPage;
import project.util.Back;


/**
 * 
 * <p> Instructor - Web page </p>
 * 
 * <p> Description: This page is set up for the Instructor, it allows them to view, edit, and post topics
 *  for the student to view. The code is based on a TilePlan for inputs based of of user preference. </p>
 * 
 * 
 * 
 * @author Group TH 58
 * 
 * @version 1.50		2024-10-9	Initial baseline
 * @version 2.00        2024-11-18  
 * 
 */



public class Instructor extends BorderPane {
	
	/**********
	 * This method is for the web page that shows the instructors view.
	 */
		//as it pops up it starts with button
    public Instructor(Stage stage, User user, DatabaseModel database) {
    	//title page
        stage.setTitle("Instructor Home page");
        
        // MANAGE HELP ARTICLES -----------------------------------------------
        Button manageHelpArticlesButton = new Button("Manage Help Articles");
        manageHelpArticlesButton.setOnAction(event -> {
        	HelpArticleDatabase h = null;
			try {
				h = new HelpArticleDatabase();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	new HelpArticleManagementPage(stage, h, 0);
        });

        // VIEW HELP ARTICLES -------------------------------------------------
        Button listArticlesButton = new Button("View Help Articles");
        listArticlesButton.setOnAction(event -> {
			new ArticleHome(stage, user, database);
		});

        // GENERAL GROUP SETTINGS ---------------------------------------------
        Button groupSettingsButton = new Button("General Group Settings");
        groupSettingsButton.setOnAction(event -> {
            // new group settings page here
        });

        // SPECIAL GROUP SETTINGS ---------------------------------------------
        Button specialGroupSettingsButton = new Button("Special Group Settings");
        specialGroupSettingsButton.setOnAction(event -> {
            new SpecialGroupSettings(stage, user, database);
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
            
        // STAGE SETUP --------------------------------------------------------
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.add(manageHelpArticlesButton, 0, 0);
        centerPane.add(listArticlesButton, 1, 0);
        centerPane.add(groupSettingsButton, 0, 1);
        centerPane.add(specialGroupSettingsButton, 1, 1);

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
        Scene s = new Scene(this, 400, 250);
        Back.pushBack(s, "Instructor Home page");
        stage.setScene(s);
        stage.show();
    }
}
