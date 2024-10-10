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

public class OneTimePassPage extends Application {
	DatabaseModel databaseModel;
	
	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		databaseModel = new DatabaseModel();
		databaseModel.connect();
		
		primaryStage.setTitle("One Time Pass");
		
        TextField password = new TextField();
        // set title for the stage
        
        Button btn = new Button();
        btn.setText("Enter");
 
        // set preferred column count
        
        
        
        btn.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent event) {
            		String code = password.getText();
            		boolean valid = databaseModel.hasCode(password.getText());
            		
            		if(valid) {
            			User user = new User();
            			user.roles = databaseModel.getCodeRoles(code);
            			// Get the current window
                        Stage stage = (Stage) btn.getScene().getWindow(); 
//                        // Close the current window
//                        stage.setScene(new Scene(new LoginService()));
                        LoginService login  = new LoginService(primaryStage, user);
            		}
                } 
            });
 
        // create a tile pane
        TilePane r = new TilePane();
 
        // create a label
        Label l = new Label("Enter Password: ");

        
//	        root.getChildren().add(btn);
//	        root.getChildren().add(username);
//	        root.getChildren().add(password);
        r.getChildren().add(l);
        r.getChildren().add(password);
        r.getChildren().add(btn);
        
        primaryStage.setScene(new Scene(r, 350, 250));
        primaryStage.show();
		
	}
}