package project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class CreateAccount extends TilePane {
	
	public CreateAccount(Stage stage, User user, DatabaseModel database) {	
		stage.setTitle("Create Account");
        TextField username = new TextField();
        TextField password = new TextField();
        TextField password2 = new TextField();

        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Log In");
 
        // set preferred column count
        username.setPrefColumnCount(7);
        
        Label l4 = new Label("Passwords do not match!");
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            	
	            	String u = username.getText();
	            	String p = password.getText();
	            	String p2 = password2.getText();
	            	
	            	if(p.equals(p2)) {
	            		user.username = u;
	            		user.password = p;
	            		
	            		database.registerUser(
	            				u, 
	            				p, 
	            				"", 
	            				user.roles, 
	            				false, 
	            				0, 
	            				new String[] {});
	            		
	            		LoginService login = new LoginService(stage, user, database);
	            	} else {
	            		username.clear();
	            		password.clear();
	            		password2.clear();
	            		getChildren().add(l4);
	            	}	            	
                } 
            });
  
        // create a label
        Label l = new Label("Enter Username: ");
        Label l2 = new Label("Enter Password: ");
        Label l3 = new Label("Re-Enter Password: ");

        
        getChildren().add(l);
        getChildren().add(username);
        getChildren().add(l2);
        getChildren().add(password);
        getChildren().add(l3);
        getChildren().add(password2);
        getChildren().add(btn);
        
        stage.setScene(new Scene(this, 350, 250));
        stage.show();
	}

}
