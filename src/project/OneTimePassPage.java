package project;

import javafx.scene.Scene;
import javafx.scene.control.*;
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
        
        btn.setOnAction(event -> {
            		String code = password.getText();
            		boolean valid = database.hasCode(password.getText());
            		
            		if(valid) {
            			user.roles = database.getCodeRoles(code);
//                        stage.setScene(new Scene(new LoginService()));
                        new CreateAccount(stage, user, database);
            		}
            });
 
        // create a tile pane
 
        // create a label
        Label l = new Label("Enter Password: ");

        Button back = new Button("Back");
        back.setOnAction(event -> {
        	stage.setScene(Back.back(stage));
        	
        });

//	        root.getChildren().add(btn);
//	        root.getChildren().add(username);
//	        root.getChildren().add(password);
        getChildren().add(l);
        getChildren().add(password);
        getChildren().add(btn);
        getChildren().add(back);
        
        Scene s = new Scene(this, 350, 250);
        Back.pushBack(s);
        stage.setScene(s);
        stage.show();
		
	}
}