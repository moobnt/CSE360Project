package project.automatedTesting.admin;

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

public class invitationSetup extends Application {
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
	        System.out.println("Login with admin account -> Invite New User -> Check any box you like -> Generate Invite Code -> Copy Code -> Back");
	        System.out.println("Log out -> Use Invitation Code -> Paste the code in -> Submit Code\n");
	        System.out.println("Enter the following information");
	        System.out.println("Username: test2.1");
	        System.out.println("Email: test2.1@gmail.com");
	        System.out.println("Password: test2.1");
	        System.out.println("Confirm Password: test2.1");

	        System.out.println("First Name: Test");
	        System.out.println("Middle Name: ");
	        System.out.println("Last Name: 2.1");
	        System.out.println("Preferred Name (Optional): test2.1");
	        
	        System.out.println("\nFinally, log in the test2.1 to verify if it is account with your assigned role(s) or not");

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
	        //database.registerUser("testUser1", "testPass1", "", new String[]{"[Student]"}, false, null, new String[]{});


	        //Valid Login with Multiple Roles
	        database.registerUser("admin", "admin", "", new String[]{"Admin"}, false, null, new String[]{"Admin1"});
	        
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

}
