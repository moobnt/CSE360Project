package project;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class UserHome extends TilePane {
    public UserHome(Stage stage, User user, DatabaseModel database) {
        stage.setTitle("User Home");

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(event -> {
            // Redirect back to login page
            new LoginService(stage, user, database);
        });

        getChildren().add(logoutButton);
        stage.setScene(new Scene(this, 300, 200));
        stage.show();
    }
}
