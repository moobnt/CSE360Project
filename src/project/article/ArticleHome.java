package project.article;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.group.SpecialGroupDatabase;
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
		SpecialGroupDatabase specialGroupDatabase = new SpecialGroupDatabase();
		
        // set content level
		Label contentLevelText = new Label();
		contentLevelText.setText("Select Content Level:");
		ComboBox<String> contentLevelSelect = new ComboBox<String>();
		contentLevelSelect.getItems().addAll(
			"Beginner",
			"Intermediate",
			"Advanced",
			"Expert",
			"All"
		);
        // set group
		Label groupTypeText = new Label();
		groupTypeText.setText("Select Group Type: ");
		ComboBox<String> groupTypeSelect = new ComboBox<String>();
		groupTypeSelect.getItems().addAll(
			"General",
			"Special Access"
		);
		Label groupText = new Label();
		groupText.setText("Select Group: ");
		ComboBox<String> groupSelect = new ComboBox<String>();
		groupSelect.setDisable(true);

		groupTypeSelect.setOnAction(event -> {
			groupSelect.setDisable(false);
			String groupType = groupTypeSelect.getValue();

			if (groupType == "General") {
				groupSelect.getItems().setAll(new String[0]);
				groupSelect.getItems().addAll(
					"Test Group"
				);
			} else if (groupType == "Special Access") {
				groupSelect.getItems().setAll(new String[0]);
				String[] userGroups = specialGroupDatabase.findGroups(user.username, user.roles);
				groupSelect.getItems().addAll(
					userGroups
				);
			} else {
				groupSelect.getItems().setAll(new String[0]);
				groupSelect.getItems().add(
					null
				);
			}
		});

		TextField searchTerm = new TextField();
		searchTerm.setPromptText("Search Term(s)");
		
        Button searchButton = new Button("Search");
		searchButton.setDisable(true);
		searchButton.setOnAction(event -> {
			// search for articles with given criteria
			// if one or more criteria is empty, prompt to try again
		});

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
		// gridpane for center
		GridPane centerGrid = new GridPane();
		centerGrid.setAlignment(Pos.CENTER);
		centerGrid.setPadding(new Insets(20));
        centerGrid.setHgap(10);
        centerGrid.setVgap(10);

		centerGrid.add(contentLevelText, 0, 0);
		centerGrid.add(contentLevelSelect, 1, 0);
		centerGrid.add(groupTypeText, 0, 1);
		centerGrid.add(groupTypeSelect, 1, 1);
		centerGrid.add(groupText, 0, 2);
		centerGrid.add(groupSelect, 1, 2);
		centerGrid.add(searchTerm, 0, 3, 2, 1);
		centerGrid.add(searchButton, 0, 4);

		// setting up gridpane for bottom buttons
		GridPane bottomGrid = new GridPane();
		bottomGrid.setAlignment(Pos.CENTER);
		bottomGrid.setPadding(new Insets(20));
        bottomGrid.setHgap(10);
        bottomGrid.setVgap(10);

		// adding all elements to gridpane
		bottomGrid.add(listArticlesButton, 0, 0, 1, 1);
		bottomGrid.add(helpButton, 1, 0);
		bottomGrid.add(logOutButton, 2, 0);
		bottomGrid.add(quitButton, 3, 0);
        
		this.setCenter(centerGrid);
        this.setBottom(bottomGrid);
		Scene s = new Scene(this, 300, 300);
        Back.pushBack(s);
		stage.setScene(s);
        stage.show();
    }
}
