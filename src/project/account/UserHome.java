package project.account;

import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
/**
 * <p> userHome class </p>
 * 
 * <p> Description: Empty user page for now</p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */
public class UserHome extends TilePane {
    public UserHome(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("User Home");

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(event -> {
            // Redirect back to login page
            try {
				new LoginService(stage, user, database);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        getChildren().add(logoutButton);
        stage.setScene(new Scene(this, 300, 200));
        stage.show();
    }
}
