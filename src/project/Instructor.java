package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Instructor extends TilePane{
	
	
		//as it pops up it starts with button
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Instructor Home page");
        
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10); // Horizontal gap between tiles
        tilePane.setVgap(10); 
        Button btn = new Button("log out");
        
        
            public void handle(ActionEvent event) {
            	@Override
	            		/***
	            		 * this event occures when the log out button is played. When the button is pressed
	            		 * it results in the application closing the current window
	            		 * and running the new window I.e the log out page
	            		 */
	            		
	                    // Start the external Java process
	                   
	                    
	                    //*****
	                    
	                    	// Get the current window
	                         
	                        // Close the current window
	                       
	                   
	                } 
	            });
            
        
        StackPane root = new StackPane();
        tilePane.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
