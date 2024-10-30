package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.sql.SQLException;

public class HelpArticleManagementPage extends TilePane {

    public HelpArticleManagementPage(Stage stage, HelpArticleDatabase helpArticleDatabase) {
        stage.setTitle("Help Article Management");

     // CreateHelpArticlePage Button ------------------------------------------------------------------------------------------
        Button createHelpArticleButton = new Button("Create Help Articles");
        createHelpArticleButton.setOnAction(event -> {
        	CreateHelpArticlePage createHelpArticlePage = new CreateHelpArticlePage(stage, helpArticleDatabase);
        });
        
        // HelpArticlesManagementPage Button ------------------------------------------------------------------------------------------
        Button updateHelpArticlesButton = new Button("Update Help Articles");
        updateHelpArticlesButton.setOnAction(event -> {
            FetchHelpArticlePage fetchHelpArticlePage = new FetchHelpArticlePage(stage, helpArticleDatabase);
        });
        
        // List Articles Button ------------------------------------------------------------------------------------------
        Button listArticlesButton = new Button("List/View Articles");
        listArticlesButton.setOnAction(event -> {
            ListArticlesPage listArticlesPage = new ListArticlesPage(stage, helpArticleDatabase);
        });
        
        // Delete Articles Button ------------------------------------------------------------------------------------------
        Button deleteArticleButton = new Button("Delete Article");
        deleteArticleButton.setOnAction(event -> {
            DeleteArticleTitlePage listArticlesPage = new DeleteArticleTitlePage(stage, helpArticleDatabase);
        });

        // Backup Articles Button ------------------------------------------------------------------------------------------
        Button backupOptionsButton = new Button("Backup Options");
        backupOptionsButton.setOnAction(event -> {
            BackupOptionsPage backupOptionsPage = new BackupOptionsPage(stage, helpArticleDatabase);
        });

     // Backup Articles Button ------------------------------------------------------------------------------------------
        Button restoreOptionsButton = new Button("Restore Articles");
        restoreOptionsButton.setOnAction(event -> {
            RestoreOptionsPage restoreOptionsPage = new RestoreOptionsPage(stage, helpArticleDatabase);
        });
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });
        
        // Add the create article button to the TilePane
        getChildren().addAll(createHelpArticleButton,updateHelpArticlesButton,listArticlesButton,deleteArticleButton,backupOptionsButton,restoreOptionsButton, back);
        
        

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 500, 600);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
