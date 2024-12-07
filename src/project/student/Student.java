package project.student;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.article.ArticleHome;
import project.util.Back;

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
public class Student extends BorderPane {
	/**********
	 * This method is for the web page that shows the Student view.
	 * 
	 * @return			Sends the user back to the log in page
	 */
    public Student(Stage stage, User user, DatabaseModel database) {
    	// Set the title in the window bar
        stage.setTitle("Student Home Page");
		Text welcomeText = new Text("Welcome, " + user.username + "\nPlease select an option:");

		// LOG OUT BUTTON -----------------------------------------------------
        Button logOutButton = new Button("Log out");
        logOutButton.setOnAction(event -> {
        	try {
				new LoginService(stage, new User(), database);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		// setting up gridpane
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

		// adding all elements to gridpane
		gridPane.add(listArticlesButton, 0, 0, 1, 1);
		gridPane.add(helpButton, 1, 0);
		gridPane.add(logOutButton, 2, 0);
		gridPane.add(quitButton, 3, 0);
        
		this.setTop(welcomeText);
		BorderPane.setAlignment(welcomeText, Pos.CENTER);
		BorderPane.setMargin(welcomeText, new Insets(20));
        this.setCenter(gridPane);
		Scene s = new Scene(this, 300, 200);
        Back.pushBack(s, "Student Home Page");
		stage.setScene(s);
        stage.show();
    }
}

