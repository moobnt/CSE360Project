package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
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



public class Instructor extends TilePane {
	
	
		//as it pops up it starts with button
    public Instructor(Stage stage, User user, DatabaseModel database) {
    	//title page
        stage.setTitle("Instructor Home page");
        
        setHgap(10); // Horizontal gap between tiles
        setVgap(10); 
        
        Button btn = new Button("log out");
        
        btn.setOnAction(new EventHandler<>() {
        	
        	public void handle(ActionEvent event) {
        		
        		//send back to login page
        		LoginService login = new LoginService(stage, user, database);

                   
                } 
            });
            
        
        getChildren().add(btn);
        stage.setScene(new Scene(this, 300, 250));
        stage.show();
    }
}
