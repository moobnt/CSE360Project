package project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

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
        
        Button btn2 = new Button();
        btn2.setText("One Time Code");
 
        // set preferred column count
        email.setPrefColumnCount(7);        
        
//        btn.setOnAction(new EventHandler<>() {
//            public void handle(ActionEvent event) {
//            	
//	            	user.email = email.getText();
//	            	user.password = password.getText();	
//	            	
//	            	if(database.getUserField(user.username, "email") != "") {
//	            		user.roles = new String[] {"Admin", "Student", "Instructor"};
//		            	Role role = new Role(primaryStage, user, database);
//	            	}
//                } 
//            });
//        
//        btn2.setOnAction(new EventHandler<>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				// TODO Auto-generated method stub
//				OneTimePassPage create = new OneTimePassPage(primaryStage, user, database);
//			}
//        	
//        });
 
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
        getChildren().add(btn2);
        
        primaryStage.setScene(new Scene(this, 350, 250));
        primaryStage.show();
	}
}