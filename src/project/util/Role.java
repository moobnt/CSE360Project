package project.util;

import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.User;
import project.admin.Admin;
import project.instructor.Instructor;
import project.student.*;
/**
 * <p> Role class </p>
 * 
 * <p> Description: Handles the role of a user</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
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
         				new Instructor(stage, user, database);
         			}
                 	
                 });
            }
            
            if(((String) o).equals("Student")) {
           	 btn.setOnAction(new EventHandler<>() {

        			@Override
        			public void handle(ActionEvent event) {
        				new Student(stage, user, database);
        			}
                	
                });
           }
            
            if(((String) o).equals("Admin")) {
            	btn.setOnAction(new EventHandler<>() {

        			@Override
        			public void handle(ActionEvent event) {
        				new Admin(stage, user, database);
        			}
                	
                });
            }
            
            
            getChildren().add(btn);
        }


        stage.setScene(new Scene(this, 400, 250));
        stage.show();

    }
}