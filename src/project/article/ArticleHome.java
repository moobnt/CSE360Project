package project.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.account.DatabaseHelper;
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

		try {
            HelpArticleDatabase helpArticleDatabase = new HelpArticleDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
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
		TextField groupNameInput = new TextField(); // Changed to TextField for group name input

		TextField searchTerm = new TextField();
		searchTerm.setPromptText("Search Term(s)");
		
        Button searchButton = new Button("Search");
        searchButton.setDisable(true);

        // Enable search button if search criteria is provided
        searchTerm.textProperty().addListener((observable, oldValue, newValue) -> toggleSearchButton(searchButton, searchTerm, groupNameInput, contentLevelSelect));

        groupNameInput.textProperty().addListener((observable, oldValue, newValue) -> toggleSearchButton(searchButton, searchTerm, groupNameInput, contentLevelSelect));

        contentLevelSelect.valueProperty().addListener((observable, oldValue, newValue) -> toggleSearchButton(searchButton, searchTerm, groupNameInput, contentLevelSelect));

        searchButton.setOnAction(event -> {
            // Search for articles with given criteria
            String title = searchTerm.getText(); // Use the title as the search term
            String groupName = groupNameInput.getText(); // Get the group name from the input
            String level = contentLevelSelect.getValue();

            if (title.isEmpty() && groupName.isEmpty() && "All".equals(level)) {
                showError("Please provide at least one search criterion.");
            } else {
                try {
                    // Construct the SQL query based on the user's input
                    StringBuilder query = new StringBuilder("SELECT * FROM help_articles WHERE 1=1");

                    if (!title.isEmpty()) {
                        query.append(" AND title LIKE ?");
                    }
                    if (!"All".equals(level)) {
                        query.append(" AND level = ?");
                    }
                    if (!groupName.isEmpty()) {
                        query.append(" AND groupIdentifier LIKE ?");
                    }

                    PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query.toString());

                    int index = 1;
                    if (!title.isEmpty()) {
                        pstmt.setString(index++, "%" + title + "%");
                    }
                    if (!"All".equals(level)) {
                        pstmt.setString(index++, level);
                    }
                    if (!groupName.isEmpty()) {
                        pstmt.setString(index, "%" + groupName + "%");
                    }

                    // Execute the query
                    ResultSet rs = pstmt.executeQuery();
                    // Process and display the search results
                    displaySearchResults(rs, groupName, level);

                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error performing search: " + e.getMessage());
                }
            }
        });

    // OPTIONS THAT ARE ALWAYS AVALIABLE --------------------------------------
		// BACK BUTTON --------------------------------------------------------
		Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

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
		centerGrid.add(groupTypeSelect, 1, 2);
		centerGrid.add(searchTerm, 0, 3, 2, 1);
		centerGrid.add(searchButton, 0, 4);

		// setting up gridpane for bottom buttons
		GridPane bottomGrid = new GridPane();
		bottomGrid.setAlignment(Pos.CENTER);
		bottomGrid.setPadding(new Insets(20));
        bottomGrid.setHgap(10);
        bottomGrid.setVgap(10);

		// adding all elements to gridpane
		bottomGrid.add(back, 0, 0);
		bottomGrid.add(listArticlesButton, 1, 0, 1, 1);
		bottomGrid.add(helpButton, 2, 0);
		bottomGrid.add(logOutButton, 3, 0);
		bottomGrid.add(quitButton, 4, 0);
        
		this.setCenter(centerGrid);
        this.setBottom(bottomGrid);
		Scene s = new Scene(this, 400, 300);
        Back.pushBack(s);
		stage.setScene(s);
        stage.show();
    }

	private void toggleSearchButton(Button searchButton, TextField searchTerm, TextField groupNameInput, ComboBox<String> contentLevelSelect) {
        boolean enableButton = !searchTerm.getText().isEmpty() || !groupNameInput.getText().isEmpty() || !contentLevelSelect.getValue().equals("All");
        searchButton.setDisable(!enableButton);
    }

    private void displaySearchResults(ResultSet rs, String groupName, String selectedLevel) throws SQLException {
        VBox searchResultsBox = new VBox();
        searchResultsBox.setSpacing(10);

        Label groupLabel = new Label("Group: " + groupName);
        searchResultsBox.getChildren().add(groupLabel);

        int levelCount = 0;
        while (rs.next()) {
            String level = rs.getString("level");
            if (level.equalsIgnoreCase(selectedLevel) || selectedLevel.equals("All")) {
                levelCount++;
            }
        }

        Label levelCountLabel = new Label("Number of articles in " + selectedLevel + ": " + levelCount);
        searchResultsBox.getChildren().add(levelCountLabel);

        rs.beforeFirst();  // Reset cursor to the start
        int count = 1;
        while (rs.next()) {
            String level = rs.getString("level");
            if (level.equalsIgnoreCase(selectedLevel) || selectedLevel.equals("All")) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String shortDescription = rs.getString("shortDescription");

                String shortForm = String.format("%d. %s by %s: %s", count++, title, author, shortDescription);
                Label articleLabel = new Label(shortForm);
                searchResultsBox.getChildren().add(articleLabel);
            }
        }

        ScrollPane scrollPane = new ScrollPane(searchResultsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        setCenter(scrollPane);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
