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

public class ViewHelpArticlePage extends ScrollPane {
    public ViewHelpArticlePage(Stage stage, HelpArticleDatabase helpArticleDatabase, String title) {
        stage.setTitle("Articles with Title: " + title);

        // Create a VBox to hold the article details
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

        try {
            List<HelpArticle> articles = helpArticleDatabase.getAllArticles(); // Fetch all articles
            vbox.getChildren().clear(); // Clear existing articles
            vbox.getChildren().add(back);

            // Filter articles based on the title
            for (HelpArticle article : articles) {
                if (article.getTitle().equalsIgnoreCase(title)) {
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
                        keywordsString,
                        article.getBody(),
                        referencesString
                    );

                    // Create a Label for each article
                    Label articleLabel = new Label(articleDetails);
                    articleLabel.setWrapText(true);
                    vbox.getChildren().add(articleLabel);
                }
            }

            // If no articles found for the title
            if (vbox.getChildren().isEmpty()) {
                Label noArticlesLabel = new Label("No articles found with title: " + title);
                vbox.getChildren().add(noArticlesLabel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading articles: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
//        });

        // Add components to the VBox
//        vbox.getChildren().add(loadArticlesButton);

        // Add the VBox to the ScrollPane
        setContent(vbox);
        setFitToWidth(true);
        setPannable(true);

        // Set the scene with the current ScrollPane
        Scene s = new Scene(this, 800, 600);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
