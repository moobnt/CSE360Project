package project.student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.account.DatabaseHelper;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.article.ArticleHome;
import project.article.HelpArticleDatabase;
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

    private ListView<String> genericMessagesListView;
    // Add a ListView for specific messages
    private ListView<String> specificMessagesListView;

    /**
     * The method that will display all FAQ entries and JavaFX elements
     */
    public HelpFAQ(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("Frequently Asked Questions");

        // OPTIONS THAT ARE ALWAYS AVAILABLE --------------------------------------
        // LOG OUT BUTTON -----------------------------------------------------
        Button logOutButton = new Button("Log out");
        logOutButton.setOnAction(event -> {
            try {
                new LoginService(stage, new User(), database);
            } catch (SQLException e) {
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
        	HelpArticleDatabase h = null;
			try {
				h = new HelpArticleDatabase();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new ArticleHome(stage, user, database, h);
        });

        // SEND GENERIC MESSAGE BUTTON ----------------------------------------
        Button sendGenericMessageButton = new Button("Send Generic Message");
        sendGenericMessageButton.setOnAction(event -> {
            // Prompt the user for their username and password
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Username");
            usernameDialog.setHeaderText("Please enter your username:");
            
            // Use a final variable to store the username
            final String[] username = new String[1]; // Array is used to make it effectively final
            usernameDialog.showAndWait().ifPresent(input -> {
                username[0] = input; // Get username input
            });

            if (username[0] != null) {
                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Enter your password");

                Alert passwordDialog = new Alert(Alert.AlertType.CONFIRMATION, 
                                                  "Enter your password:", ButtonType.OK);
                passwordDialog.getDialogPane().setContent(passwordField);
                passwordDialog.showAndWait().ifPresent(response -> {
                    String password = passwordField.getText();
                    // Validate username and password
                    if (isValidUser(username[0], password)) {
                        // If valid, proceed to ask for the generic message
                        TextInputDialog messageDialog = new TextInputDialog();
                        messageDialog.setTitle("Generic Message");
                        messageDialog.setHeaderText("Please enter your generic message:");

                        messageDialog.showAndWait().ifPresent(message -> {
                            // Store the generic message
                            try {
                                sendGenericMessage(username[0], message);
                                // After storing, list all generic messages
                                listGenericMessages();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        // Show error if invalid credentials
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
                        errorAlert.showAndWait();
                    }
                });
            }
        });

        // SEND SPECIFIC MESSAGE BUTTON --------------------------------------
        Button sendSpecificMessageButton = new Button("Send Specific Message");
        sendSpecificMessageButton.setOnAction(event -> {
            // Prompt the user for their username and password
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Username");
            usernameDialog.setHeaderText("Please enter your username:");

            // Use a final variable to store the username
            final String[] username = new String[1]; // Array is used to make it effectively final
            usernameDialog.showAndWait().ifPresent(input -> {
                username[0] = input; // Get username input
            });

            if (username[0] != null) {
                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Enter your password");

                Alert passwordDialog = new Alert(Alert.AlertType.CONFIRMATION, 
                                                  "Enter your password:", ButtonType.OK);
                passwordDialog.getDialogPane().setContent(passwordField);
                passwordDialog.showAndWait().ifPresent(response -> {
                    String password = passwordField.getText();
                    // Validate username and password
                    if (isValidUser(username[0], password)) {
                        // If valid, prompt for the specific message
                        TextField queryField = new TextField();
                        queryField.setPromptText("Describe the help article you cannot find");

                        Alert specificMessageDialog = new Alert(Alert.AlertType.CONFIRMATION, 
                                                                 "Enter the specific help you are looking for:", ButtonType.OK);
                        specificMessageDialog.getDialogPane().setContent(queryField);
                        specificMessageDialog.showAndWait().ifPresent(queryResponse -> {
                            String query = queryField.getText();
                            // Ask for previous search requests
                            TextInputDialog searchHistoryDialog = new TextInputDialog();
                            searchHistoryDialog.setTitle("Search History");
                            searchHistoryDialog.setHeaderText("Enter previous search requests (separated by commas):");
                            
                            searchHistoryDialog.showAndWait().ifPresent(searchHistory -> {
                                // Store the specific message and search history
                                String fullMessage = query + "\nSearch History: " + searchHistory;

                                // Store the specific message and search history in the database
                                sendSpecificMessageWithSearchHistory(username[0], fullMessage);
                                
                                try {
									listSpecificMessages();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

                                // Optionally, show an alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                                                        "Your specific message has been sent!", ButtonType.OK);
                                alert.showAndWait();
                            });
                        });
                    } else {
                        // Show error if invalid credentials
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
                        errorAlert.showAndWait();
                    }
                });
            }
        });
        
        // LIST GENERIC MESSAGES BUTTON ---------------------------------------------
        Button listGenericMessagesButton = new Button("List Generic Messages");
        listGenericMessagesButton.setOnAction(event -> {
            try {
                listGenericMessages();  // Display the stored messages
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        // LIST SPCIFIC MESSAGES BUTTON ---------------------------------------------
        Button listSpecificMessagesButton = new Button("List Specific Messages");
        listSpecificMessagesButton.setOnAction(event -> {
            try {
            	listSpecificMessages();  // Display the stored messages
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // STAGE SETUP --------------------------------------------------------
        // setting up gridpane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Adding all elements to gridpane
        gridPane.add(listArticlesButton, 0, 0, 1, 1); // Row 0, Column 0
        gridPane.add(helpButton, 1, 0);  // Row 0, Column 1
        gridPane.add(logOutButton, 2, 0);  // Row 0, Column 2
        gridPane.add(quitButton, 3, 0);  // Row 0, Column 3

        gridPane.add(sendGenericMessageButton, 0, 1);  // Row 1, Column 0
        gridPane.add(sendSpecificMessageButton, 1, 1);  // Row 1, Column 1

        gridPane.add(listGenericMessagesButton, 2, 1);  // Row 1, Column 2
        gridPane.add(listSpecificMessagesButton, 3, 1);  // Row 1, Column 3

        this.setBottom(gridPane);

        // Create VBox to contain both ListViews
        VBox listViewContainer = new VBox();
        listViewContainer.setSpacing(10);  // Add spacing between the lists

        // Create ListView for generic messages
        genericMessagesListView = new ListView<>();
        listViewContainer.getChildren().add(genericMessagesListView);

        // Create ListView for specific messages
        specificMessagesListView = new ListView<>();
        listViewContainer.getChildren().add(specificMessagesListView);

        // Set the VBox with both ListViews to the center
        this.setCenter(listViewContainer);
        // Set the scene with the current TilePane
        Scene s = new Scene(this, 600, 600);
        Back.pushBack(s, "Frequently Asked Questions");
        stage.setScene(s);
        stage.show();
    }

    /**
     * Method to validate the username and password (example placeholder)
     */
    private boolean isValidUser(String username, String password) {
        try {
            // Replace with actual validation logic, e.g., check against the database
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                return rs.next(); // If a record is returned, the user exists with the provided credentials
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to send a generic message to the help system.
     * @param username The student’s username.
     * @param message The generic message.
     */
    private void sendGenericMessage(String username, String message) {
        try {
            // Insert the generic message into the database
            String query = "INSERT INTO help_messages (username, message, type) VALUES (?, ?, 'generic')";
            try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, message);
                pstmt.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your generic message has been sent!", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error sending message: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    /**
     * Method to list all generic messages from the help system.
     * Displays messages in the ListView in the center of the page.
     */
    private void listGenericMessages() throws SQLException {
        String query = "SELECT username, message, created_at FROM help_messages WHERE type = 'generic' ORDER BY created_at";
        
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            int count = 1;
            genericMessagesListView.getItems().clear();  // Clear existing items in ListView
            while (rs.next()) {
                String username = rs.getString("username");
                String message = rs.getString("message");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // Add messages to ListView with index
                genericMessagesListView.getItems().add(count + ". " + username + ": " + message + " (Sent on: " + createdAt + ")");
                count++;
            }
        }
    }

    /**
     * Method to send a specific message to the help system along with search queries.
     * @param username The student’s username.
     * @param query The specific query describing what the student needs.
     */
    private void sendSpecificMessage(String username, String query) {
        try {
            // Store the query into the database as a specific message
            String insertQuery = "INSERT INTO help_messages (username, message, type) VALUES (?, ?, 'specific')";
            try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(insertQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, query);
                pstmt.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your specific message has been sent!", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error sending message: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    // Method to send a specific message with search history
    private void sendSpecificMessageWithSearchHistory(String username, String message) {
        try {
            // Insert the specific message with search history into the database
            String query = "INSERT INTO help_messages (username, message, type) VALUES (?, ?, 'specific')";
            try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, message);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error sending message: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    // Method to list all specific messages from the help system
    private void listSpecificMessages() throws SQLException {
        String query = "SELECT username, message, created_at FROM help_messages WHERE type = 'specific' ORDER BY created_at";
        
        try (PreparedStatement pstmt = DatabaseHelper.getConnection().prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            int count = 1;
            specificMessagesListView.getItems().clear();  // Clear existing items in ListView
            while (rs.next()) {
                String username = rs.getString("username");
                String message = rs.getString("message");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // Add messages to ListView with index
                specificMessagesListView.getItems().add(count + ". " + username + ": " + message + " (Sent on: " + createdAt + ")");
                count++;
            }
        }
    }
}
