package project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

/**
 * <p> FinishSetup class </p>
 * 
 * <p> Description: Used to set up the user account/finish setting up a user account </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */


public class FinishSetupPage extends TilePane {
	
	public FinishSetupPage(Stage primaryStage, User user, DatabaseModel database) {
		
		primaryStage.setTitle("Instructor Home page");
        TextField email = new TextField();
        TextField first = new TextField();
        TextField middle = new TextField();
        TextField last = new TextField();
        TextField preferred = new TextField();
        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Log In");
        
 
        // set preferred column count
        email.setPrefColumnCount(7);        
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            	
	            	user.email = email.getText();
	            	
	            	FullName name = new FullName();
	            	
	            	name.first = first.getText();
	            	name.middle =middle.getText();
	            	name.last = last.getText();
	            	name.preferred = preferred.getText();
	            	
	            	user.fullName = name;
	            	
	            	database.editUser(user.username, "email", user.email);
	            	database.editUser(user.username, "fullName", new String[] {name.first, name.middle, name.last, name.preferred});
	            	
		            new Role(primaryStage, user, database);
                } 
            });
 
        // create a label
        Label l = new Label("Enter Email: ");
        Label l2 = new Label("First Name: ");
        Label l3 = new Label("Middle Name: ");
        Label l4 = new Label("Last Name: ");
        Label l5 = new Label("Preferred Name: ");
        
        getChildren().add(l);
        getChildren().add(email);
        getChildren().add(l2);
        getChildren().add(first);
        getChildren().add(l3);
        getChildren().add(middle);
        getChildren().add(l4);
        getChildren().add(last);
        getChildren().add(l5);
        getChildren().add(preferred);
        getChildren().add(btn);
        
        primaryStage.setScene(new Scene(this, 350, 250));
        primaryStage.show();
	}
}