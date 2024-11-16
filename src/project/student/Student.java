package project.student;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.DatabaseModel;
import project.LoginService;
import project.User;

/**
 * 
 * <p> Student - Web page </p>
 * 
 * <p> Description: This page is set up for the Student, it allows them to view and request posts. 
 * The code is based on a TilePane for inputs based of of user preference. </p>
 * 
 * 
 * @author Group TH 58
 * 
 * @version 1.50		2024-10-9	Initial baseline
 * @version 2.00		2024-11-16 	Added additional buttons
 * 
 */
public class Student extends TilePane {
	/**********
	 * This method is for the web page that shows the Student view.
	 * 
	 * @return			Sends the user back to the log in page
	 */
    public Student(Stage stage, User user, DatabaseModel database) {
    	// Set the title in the window bar
        stage.setTitle("Student Home Page");
		System.out.println("Student Home Page");
        
		// LOG OUT BUTTON -----------------------------------------------------
        Button logOutButton = new Button("Log out");
        logOutButton.setOnAction(event -> {
        	new LoginService(stage, user, database);
            });

		// QUIT BUTTON --------------------------------------------------------
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

		// HELP BUTTON --------------------------------------------------------
		Button helpButton = new Button("Help");
		helpButton.setOnAction(event -> {
			new HelpFAQ(stage, user, database);
		});

		// LIST ARTICLES BUTTON -----------------------------------------------
		Button listArticlesButton = new Button("Articles");
		listArticlesButton.setOnAction(event -> {
			new ArticleHome(stage, user, database);
		});
            
		// STAGE SETUP --------------------------------------------------------
        getChildren().addAll(logOutButton, quitButton, helpButton, listArticlesButton);
        stage.setScene(new Scene(this, 300, 250));
        stage.show();
    }
}

