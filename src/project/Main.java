package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class Main extends Application {
    DatabaseModel databaseModel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        User user = new User();

        databaseModel = new DatabaseModel();
        databaseModel.connect();
        
        Back.initBack();

        // Start at the login page
        new LoginService(primaryStage, user, databaseModel);
    }
}
