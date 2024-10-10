package project;

import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import project.DatabaseHelper;

public class Main {
	
	
	public static void main(String[] args) {
		
		DatabaseModel databaseModel = new DatabaseModel();
		
		databaseModel.connect();
		databaseModel.resetTables();
		databaseModel.registerUser(
				"asmithey", 
				"pw", 
				"aasmithe@asu.edu", 
				new String[] {"Admin", "Student"}, 
				false,
				new Date().getTime() + TimeUnit.DAYS.toMillis(1),
				new String[] {"Adam", "A", "Smithey", "Adam"});
		
		databaseModel.registerCode(
				"hello", 
				"Student", "Admin");
		
		databaseModel.editUser("asmithey", "password", "pass");
		System.out.println(databaseModel.getUserField("asmithey","password"));

		databaseModel.addCodeRoles("hello", "Instructor");
		
		databaseModel.removeCode("hello");
		System.out.println(databaseModel.getCodeRoles("hello").length);

		
	}
	
}


