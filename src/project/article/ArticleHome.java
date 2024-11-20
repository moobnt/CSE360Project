package project.article;

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
import project.student.HelpFAQ;
import project.util.Back;

/**
 * <p> ArticleHome Class </p>
 * 
 * <p> Description: This is the student landing page for finding articles.
 * From here, a student can see all articles in a group, search for articles,
 * or see all articles. </p>
 * 
 * @author Group TH 58
 * 
 * @version 1.00        2024-11-16 Class Created
 */
public class ArticleHome extends BorderPane {
    /**
     * This method displays all options for searching or displaying articles
     */
    public ArticleHome(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Article Home Page");
        // set content level
        // set group
        // search

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
