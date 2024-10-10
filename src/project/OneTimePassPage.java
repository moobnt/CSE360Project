package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class OneTimePassPage extends TilePane {
	
	public OneTimePassPage(Stage stage, User user, DatabaseModel database) {
		
		
		stage.setTitle("One Time Pass");
		
        TextField password = new TextField();
        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Enter");
 
        // set preferred column count
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            		String code = password.getText();
            		boolean valid = database.hasCode(password.getText());
            		
            		if(valid) {
            			user.roles = database.getCodeRoles(code);
            			// Get the current window
                        Stage stage = (Stage) btn.getScene().getWindow(); 
//                        // Close the current window
//                        stage.setScene(new Scene(new LoginService()));
                        CreateAccount login  = new CreateAccount(stage, user, database);
            		}
                } 
            });
 
        // create a tile pane
 
        // create a label
        Label l = new Label("Enter Password: ");

        
//	        root.getChildren().add(btn);
//	        root.getChildren().add(username);
//	        root.getChildren().add(password);
        getChildren().add(l);
        getChildren().add(password);
        getChildren().add(btn);
        
        stage.setScene(new Scene(this, 350, 250));
        stage.show();
		
	}
}