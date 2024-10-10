package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Instructor extends Application {
	
<<<<<<< HEAD
//	public static void main(String[] args) {
//		System.out.println("helo");
//	}
	//launches button
		public static void main(String[] args) {
	        launch(args);
	    }
	    
		//as it pops up it starts with button
=======
	//launches button
	public static void main(String[] args) {
        launch(args);
    }
	
	//as it pops up it starts with button
>>>>>>> branch 'main' of https://github.com/moobnt/CSE360Project.git
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Instructor Home page");
        Button btn = new Button();
        btn.setText("Log Out");
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            	
	            		/***
	            		 * this event occures when the log out button is played. When the button is pressed
	            		 * it results in the application closing the current window
	            		 * and running the new window I.e the log out page
	            		 */
	            		
	                    // Start the external Java process
//	                   LoginService.main(new String[0]);
//	                    
//	                    //*****
//	                    
//	                    	// Get the current window
//	                        Stage stage = (Stage) btn.getScene().getWindow(); 
//	                        // Close the current window
//	                        stage.close(); 
//	                   
	                } 
	            });
            
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
