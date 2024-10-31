package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


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
 * @version 1.00		2024-10-9	Initial baseline
 * 
 */



public class Instructor extends StackPane {
	
	/**********
	 * This method is for the web page that shows the instructors view, which is the log out button.
	 * 
	 * @param input		when the button is pressed the 
	 * @return			send the user back to the log in page
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
        		LoginService login = new LoginService(stage, user, database);
                   
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
