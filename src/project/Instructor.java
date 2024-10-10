package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Instructor extends TilePane {
	
	
		//as it pops up it starts with button
    public Instructor(Stage stage, User user, DatabaseModel database) {
    	
        stage.setTitle("Instructor Home page");
        
        setHgap(10); // Horizontal gap between tiles
        setVgap(10); 
        
        Button btn = new Button("log out");
        
        btn.setOnAction(new EventHandler<>() {
        	
        	public void handle(ActionEvent event) {
        
        		LoginService login = new LoginService(stage, user, database);

                   
                } 
            });
            
        
        getChildren().add(btn);
        stage.setScene(new Scene(this, 300, 250));
        stage.show();
    }
}
