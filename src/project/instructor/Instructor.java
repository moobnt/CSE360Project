package project.instructor;

import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.article.HelpArticleDatabase;
import project.article.HelpArticleManagementPage;
import project.util.Back;


/**
 * 
 * <p> Instructor - Web page </p>
 * 
 * <p> Description: This page is set up for the Instructor, it allows them to view, edit, and post topics
 *  for the student to view. The code is based on a TilePlan for inputs based of of user preference. </p>
 * 
 * 
 * 
 * @author Group TH 58
 * 
 * @version 1.50		2024-10-9	Initial baseline
 * @version 2.00        2024-11-18  
 * 
 */



public class Instructor extends StackPane {
	
	/**********
	 * This method is for the web page that shows the instructors view.
	 */
		//as it pops up it starts with button
    public Instructor(Stage stage, User user, DatabaseModel database) {
    	//title page
        stage.setTitle("Instructor Home page");
        
        Button manageHelpArticlesButton = new Button("Manage Help Articles");
        manageHelpArticlesButton.setOnAction(event -> {
        	HelpArticleDatabase h = new HelpArticleDatabase();
        	new HelpArticleManagementPage(stage, h, 0);
        });
        
        Button btn = new Button("Log out");
        
        btn.setOnAction(new EventHandler<>() {
        	
        	public void handle(ActionEvent event) {
        		
        		//send back to login page
        		new LoginService(stage, null, database);
                   
                } 
            });
            
        HBox box = new HBox(manageHelpArticlesButton, btn);
        getChildren().addAll(box);
        
        Scene s = new Scene(this, 300, 250);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
    }
}
