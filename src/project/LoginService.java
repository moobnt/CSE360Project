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
	
	public LoginService(Stage stage, User user) {
		
		stage.setTitle("Instructor Home page");
        TextField username = new TextField();
        TextField password = new TextField();
        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Log In");
 
        // set preferred column count
        username.setPrefColumnCount(7);
        
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            	
	            	user.username = username.getText();
	            	user.password = password.getText();	            	
	            	
                } 
            });
 
        // create a tile pane
        TilePane r = new TilePane();
 
        // create a label
        Label l = new Label("Enter Username: ");
        Label l2 = new Label("Enter Password: ");

        
        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        root.getChildren().add(username);
//        root.getChildren().add(password);
        getChildren().add(l);
        getChildren().add(username);
        getChildren().add(l2);
        getChildren().add(password);
        getChildren().add(btn);
        
        stage.setScene(new Scene(this, 350, 250));
        stage.show();
	}

}
