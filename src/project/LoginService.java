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

public class LoginService extends TilePane {
	
	public LoginService(Stage primaryStage, User user, DatabaseModel database) {
		
		primaryStage.setTitle("Instructor Home page");
        TextField username = new TextField();
        TextField password = new TextField();
        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Log In");
        
        Button btn2 = new Button();
        btn2.setText("One Time Code");
 
        // set preferred column count
        username.setPrefColumnCount(7);        
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            	
	            	user.username = username.getText();
	            	user.password = password.getText();	
	            	
//	            	if(database.user)
	            	user.roles = new String[] {"Admin", "Student", "Instructor"};
	            	Role role = new Role(primaryStage, user, database);
 	            	
                } 
            });
        
        btn2.setOnAction(new EventHandler<>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				OneTimePassPage create = new OneTimePassPage(primaryStage, user, database);
			}
        	
        });
 
        // create a label
        Label l = new Label("Enter Username: ");
        Label l2 = new Label("Enter Password: ");

        getChildren().add(l);
        getChildren().add(username);
        getChildren().add(l2);
        getChildren().add(password);
        getChildren().add(btn);
        getChildren().add(btn2);
        
        primaryStage.setScene(new Scene(this, 350, 250));
        primaryStage.show();
	}
}
