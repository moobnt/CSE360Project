package project.article;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.util.Back;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * <p> ListArticles by GroupPage class </p>
 * 
 * <p> Description: Fetches the articles and organizes them based on group, and uses vBox to hold the article details </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class ListArticlesByGroupPage extends ScrollPane {

    public ListArticlesByGroupPage(Stage stage, HelpArticleDatabase helpArticleDatabase, String groupIDs) {
        stage.setTitle("Articles in Groups: " + groupIDs);

        // Create a VBox to hold the article details
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Add spacing between articles

        // Button to load articles
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });
        
        try {
            // Fetch all articles
            List<HelpArticle> articles = helpArticleDatabase.getAllArticles();
            vbox.getChildren().clear(); // Clear existing articles
            vbox.getChildren().add(back);

            // Split the group IDs and trim spaces
            List<String> groupIdList = Arrays.stream(groupIDs.split(","))
                                              .map(String::trim)
                                              .collect(Collectors.toList());

            // Filter articles based on the group IDs
            for (HelpArticle article : articles) {
                String[] articleGroupIds = article.getGroupIdentifierArray(); // Get group IDs for the article

                // Check if the article belongs to all specified group IDs
                boolean belongsToAllGroups = groupIdList.stream()
                        .allMatch(groupId -> Arrays.asList(articleGroupIds).contains(groupId));

                if (belongsToAllGroups) {
                    // Convert Object[] to String for keywords
                    Object[] keywordsArray = article.getKeywords();
                    String keywordsString = Arrays.stream(keywordsArray)
                                                  .map(Object::toString)
                                                  .collect(Collectors.joining(", "));

                    // Convert Object[] to String for reference links
                    Object[] referenceLinksArray = article.getReferenceLinks();
                    String referencesString = Arrays.stream(referenceLinksArray)
                                                    .map(Object::toString)
                                                    .collect(Collectors.joining(", "));

                    // Create a formatted string for each article
                    String articleDetails = String.format(
                        "Title: %s\nLevel: %s\nGroup Identifier: %s\nShort Description: %s\nKeywords: %s\nBody: %s\nReference Links: %s\n\n",
                        article.getTitle(),
                        article.getLevel(),
                        article.getGroupIdentifier(),
                        article.getShortDescription(),
                        keywordsString, // Use the generated keywords string
                        article.getBody(),
                        referencesString // Use the generated references string
                    );

                    // Create a Label for each article
                    Label articleLabel = new Label(articleDetails);
                    articleLabel.setWrapText(true); // Enable text wrapping
                    vbox.getChildren().add(articleLabel); // Add the article label to the VBox
                }
            }

            // If no articles found for the group IDs
            if (vbox.getChildren().isEmpty()) {
                Label noArticlesLabel = new Label("No articles found for group IDs: " + groupIDs);
                vbox.getChildren().add(noArticlesLabel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading articles: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

        // Add the VBox to the ScrollPane
        setContent(vbox);
        setFitToWidth(true); // Make the ScrollPane fit the width of the stage
        setPannable(true); // Allow panning

        // Set the scene with the current ScrollPane
        Scene s = new Scene(this, 800, 600);
        Back.pushBack(s);
        stage.setScene(s); // Increase the window size for better readability
        stage.show();
    }
}
