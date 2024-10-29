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

public class Role extends TilePane {
	
    public Role(Stage stage, User user, DatabaseModel database) {
    	
    	database.registerCode("string", "Instructor");

        //set title for stage
        stage.setTitle("Roles");

        //create a tilepane and stack pane

        for(Object o : user.roles) {
            Button btn  = new Button();
            btn.setText((String) o);
            
            if(((String) o).equals("Instructor")) {
            	 btn.setOnAction(new EventHandler<>() {

         			@Override
         			public void handle(ActionEvent event) {
         				// TODO Auto-generated method stub
         				Instructor i = new Instructor(stage, user, database);
         			}
                 	
                 });
            }
            
            if(((String) o).equals("Student")) {
           	 btn.setOnAction(new EventHandler<>() {

        			@Override
        			public void handle(ActionEvent event) {
        				// TODO Auto-generated method stub
        				Student i = new Student(stage, user, database);
        			}
                	
                });
           }
            
            if(((String) o).equals("Admin")) {
            	btn.setOnAction(new EventHandler<>() {

        			@Override
        			public void handle(ActionEvent event) {
        				// TODO Auto-generated method stub
        				Admin i = new Admin(stage, user, database);
        			}
                	
                });
            }
            
            
            getChildren().add(btn);
        }


        stage.setScene(new Scene(this, 400, 250));
        stage.show();

    }
}