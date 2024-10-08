package project;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import project.DatabaseHelper;

public class Main {
	
	
	public static void main(String[] args) {
		DatabaseHelper databaseHelper = new DatabaseHelper();
		
		try {
			databaseHelper.connectToDatabase();
			databaseHelper.clearDatabase();
			
			// register example
			databaseHelper.register(
					"asmithey", 
					"password", 
					"aasmithe@asu.edu", 
					new String[] {"Admin", "Student"}, 
					false,
					new java.sql.Date(new Date().getTime() + TimeUnit.DAYS.toMillis(1)),
					new String[] {"Adam", "A", "Smithey", "Adam"});
			
			// update user
			databaseHelper.update("asmithey", "onetimeDate", new java.sql.Date(new Date().getTime() + TimeUnit.DAYS.toMillis(2)));
			
			// display all users
			databaseHelper.displayUsersByUser();
			
			// testing
			System.out.println("\n" + databaseHelper.doesUserExist("asmithey"));
			System.out.println(databaseHelper.doesUserExist("bsmithey"));
			
			// delete user
			databaseHelper.removeUser("asmithey");
			
			// prove user is removed
			databaseHelper.displayUsersByUser();
			
			// close connection
			databaseHelper.closeConnection();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}