package project.article;

import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.article.backup.BackupOptionsPage;
import project.article.backup.RestoreOptionsPage;
import project.util.Back;
import project.account.DatabaseHelper;

/**
 * <p> HelpArticleManagmentPage class </p>
 * 
 * <p> Description: Handles the page the user see's taking in their input to send 
 * them to the next appropriate page </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class HelpArticleManagementPage extends TilePane {
    public HelpArticleManagementPage(Stage stage, HelpArticleDatabase helpArticleDatabase, int num) {
        stage.setTitle("Help Article Management");

        // CreateHelpArticlePage Button ------------------------------------------------------------------------------------------
        Button createHelpArticleButton = new Button("Create Help Articles");
        createHelpArticleButton.setOnAction(event -> {
            // Assuming 'user' is an object that contains the current user's data
            String username = null;
			try {
				username = DatabaseHelper.getUsername();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            boolean isInstructor = false;
			try {
				isInstructor = DatabaseHelper.isInstructor();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  // Assuming this method exists

            new CreateHelpArticlePage(stage, helpArticleDatabase, username, isInstructor);
        });

        
        // HelpArticlesManagementPage Button ------------------------------------------------------------------------------------------
        Button updateHelpArticlesButton = new Button("Update Help Articles");
        updateHelpArticlesButton.setOnAction(event -> {
            new FetchHelpArticlePage(stage, helpArticleDatabase);
        });
        
        // List Articles Button ------------------------------------------------------------------------------------------
        Button listArticlesButton = new Button("List/View Articles");
        listArticlesButton.setOnAction(event -> {
            new ListArticlesPage(stage, helpArticleDatabase);
        });
        
        // Delete Articles Button ------------------------------------------------------------------------------------------
        Button deleteArticleButton = new Button("Delete Article");
        deleteArticleButton.setOnAction(event -> {
            new DeleteArticleTitlePage(stage, helpArticleDatabase);
        });
        
        // Special Access Button ------------------------------------------------------------------------------------------
        Button specialAccessButton = new Button("Special Access Group");
        specialAccessButton.setOnAction(event -> {
            // Redirect user to SpecialAccessPage where they can provide group name
            String groupName = getGroupNameFromUser(); // Implement a method to get group name input from user
            try {
            	if (HelpArticleDatabase.isUserAdminInGroup(groupName)) {
                    // If the user is an admin of the group, redirect them to the SpecialAdminPage
                    new SpecialAdminPage(stage, helpArticleDatabase, groupName);
                } else if (HelpArticleDatabase.isUserViewableInGroup(groupName)) {
                    // If the user is viewable in the group, redirect them to the SpecialViewPage
                    new SpecialViewPage(stage, helpArticleDatabase, groupName);
                } else {
                    // If the user doesn't have access to the group
                    showError("You don't have access to this group.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error checking admin rights: " + e.getMessage());
            }
        });
        
        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });
        
        
        // Add the create article button to the TilePane base on whether it is Admin or Instructor
        if ( num == 1) {
        	// Backup Articles Button ------------------------------------------------------------------------------------------
            Button backupOptionsButton = new Button("Backup Options");
            backupOptionsButton.setOnAction(event -> {
                new BackupOptionsPage(stage, helpArticleDatabase);
            });

            // Backup Articles Button ------------------------------------------------------------------------------------------
            Button restoreOptionsButton = new Button("Restore Articles");
            restoreOptionsButton.setOnAction(event -> {
                new RestoreOptionsPage(stage, helpArticleDatabase);
            });
            
            getChildren().addAll(createHelpArticleButton,updateHelpArticlesButton,listArticlesButton,deleteArticleButton,backupOptionsButton,restoreOptionsButton,specialAccessButton, back);
        }
        
        else {
        	getChildren().addAll(createHelpArticleButton,updateHelpArticlesButton,listArticlesButton,deleteArticleButton,back);
        }
        

        // Set the scene with the current TilePane
        Scene s = new Scene(this, 500, 600);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    // Implement this method to get the group name from the user
    private String getGroupNameFromUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Group Name");
        dialog.setHeaderText("Enter the name of the special access group");
        dialog.setContentText("Group Name:");
        return dialog.showAndWait().orElse(null);
    }
}
