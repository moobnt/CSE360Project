package project.student;

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
import project.article.ArticleHome;
import project.util.Back;

/**
 * <p> HelpFAQ Class </p>
 * 
 * <p> Description: This page is for any frequently asked questions that students may have.
 * If a student has a specific question that is not listed on this page, they are taken
 * to a question form. </p>
 * 
 * @author Group TH 58
 * 
 * @version 1.00        2024-11-16 Class created
 */
public class HelpFAQ extends BorderPane {
    /**
     * The method that will display all FAQ entries and JavaFX elements
     */
    public HelpFAQ(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Frequently Asked Questions");

    // OPTIONS THAT ARE ALWAYS AVALIABLE --------------------------------------
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
        
        this.setBottom(gridPane);
		Scene s = new Scene(this, 600, 600);
        Back.pushBack(s);
		stage.setScene(s);
        stage.show();
    }
}
