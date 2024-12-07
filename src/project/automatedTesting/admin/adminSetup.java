package project.automatedTesting.admin;


import javafx.application.Application;
import javafx.stage.Stage;
import project.account.DatabaseModel;
import project.account.LoginService;
import project.account.User;
import project.util.Back;

public class adminSetup extends Application {
	DatabaseModel databaseModel;

	 @Override
	    public void start(Stage primaryStage) throws Exception {
	        // Initialize DatabaseModel
	        DatabaseModel database = new DatabaseModel(); // Ensure this connects to your actual database
	        database.connect();
	        
	        Back.initBack();

	        // Test First Account Creation: Empty Database - Admin Setup
	        System.out.println("Test 1: Checking Admin Setup on Empty Database");
	        database.resetTables(); // Optional: Reset or ensure database is empty
	        User user = new User();
	        new LoginService(primaryStage, user, database);
	        System.out.println("Admin setup page should appear if the database is empty.\n");
	        System.out.println("Enter the following information:");
	        System.out.println("Username: admin");
	        System.out.println("Password: admin");
	        System.out.println("Confirm Password: admin");
	        System.out.println("Email: admin@gmail.com");
	        System.out.println("First Name: First");
	        System.out.println("Middle Name: ");
	        System.out.println("Last Name: Admin");
	        System.out.println("Preferred Name (Optional): admin");
	        System.out.println("Roles: Admin");
	        System.out.println("\nThe test is successful when the output is reached. "
	        		+ "\nAdditionally, in the list of all users, there should be an admin and we can "
	        		+ "\nlog in the “admin” user again as an admin privilege.");
	        //MultipleAccountCreation(user, database);

	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

}
