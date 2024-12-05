package Tuan;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javafx.application.Application;
import javafx.stage.Stage;
import project.account.CreateAccount;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class Test3 extends Application {
	DatabaseModel databaseModel;

	 @Override
	    public void start(Stage primaryStage) throws Exception {
	        // Initialize DatabaseModel
	        DatabaseModel database = new DatabaseModel(); // Ensure this connects to your actual database
	        database.connect();
	        
	        Back.initBack();

	        //reset data for tests
	        database.resetTables(); // Optional: Reset or ensure database is empty
	        User user = new User();
	        
	        //Admin Setup
	        System.out.println("Create admin account with username and password is admin");
	        MultipleAccountCreation(database);
	        
	        //Login using admin
	        new LoginService(primaryStage, user, database);
	        System.out.println("\nINSTRUCTIONS:");
	        System.out.println("Login with admin account -> Reset a User -> Type in Reset User Account : testUser1 \n-> Submit -> Copy Code -> Back.");
	        System.out.println("Log out -> Reset Account -> Put the following information in -> Reset Password.\n");
	        System.out.println("Username: userTest1");
	        System.out.println("Enter One-Time Coded: copied one-time code");
	        System.out.println("New Password: userTest1");
	        System.out.println("Confirm Password: userTest1");

	        
	        System.out.println("\nFinally, log out and verify if the one-time Code or old password can still used to log in");

	    }

	    /**
	     * Provide some of the case for admin work.
	     *
	     * @param user     the user attempting to log in
	     * @param database the database model
	     * @throws SQLException in case of database errors
	     */
	 	private void MultipleAccountCreation(DatabaseModel database) throws SQLException {
	    	// Valid Login with Single Role
	    	System.out.println("Create student account with username is testUser1 and password is testPass1");
	        database.registerUser("testUser1", "testPass1", "", new String[]{"[Student]"}, false, null, new String[]{});


	        //Valid Login with Multiple Roles
	        System.out.println("Create admin account with username and password is admin");
	        database.registerUser("admin", "admin", "", new String[]{"[Admin]"}, false, null, new String[]{"Admin1"});
	        
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

}
