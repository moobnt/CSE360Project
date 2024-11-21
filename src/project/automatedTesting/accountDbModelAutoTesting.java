package project.automatedTesting;


import java.sql.Connection;
import java.sql.SQLException;

import project.account.DatabaseModel;

/**
 * <p> accountDbModelAutoTesting class </p>
 * 
 * <p> Description: Automated Testing for project.account.DatabaseModel with some of the test to check if it really work </p>
 * 
 * @version 
 */

public class accountDbModelAutoTesting {
	static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nTesting Automation");
        
        performTestCase(1, "testUser", true, "User exists check");
        performTestCase(2, "nonExistentUser", false, "User exists check");
        
        performTestCase(3, "thaituan", true, "Admin role check");
        performTestCase(4, "testUser", false, "Admin role check");


        performTestCase(5, "testUser", false, "One-time code validation");
        
        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performTestCase(int testCase, String username, boolean expectedResult, String testName) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Test Name: " + testName);
        System.out.println("Username: \"" + username + "\"");
        System.out.println("______________");

        DatabaseModel dbModel = new DatabaseModel();
        try {
        	dbModel.connect();
        	
        	boolean result = false;

            switch (testCase) {
                case 1:
                    // Test if user exists
                    result = dbModel.doesUserExist(username);
                    break;
                case 2:
                    // Test if non-existent user exists
                    result = dbModel.doesUserExist(username);
                    break;
                case 3:
                    // Test if user has Admin role
                    result = dbModel.isUserAdmin(username);
                    break;
                case 4:
                    // Test if user has Admin role for non-admin
                    result = dbModel.isUserAdmin(username);
                    break;
                case 5:
                    // Test if one-time code is valid
                    result = dbModel.validateOneTimeCode(username, "abcdabcde");
                    break;
            }
        
        

        if (result == expectedResult) {
            System.out.println("***Success*** The test passed for user <" + username + ">.\n");
            numPassed++;
        } else {
            System.out.println("***Failure*** The test failed for user <" + username + ">.\n");
            numFailed++;
        }
        
        displayEvaluation(dbModel);
    } catch (SQLException e) {
        System.err.println("Database connection error: " + e.getMessage());
        numFailed++;
    } finally {
        try {
            // Disconnect to prevent connection leaks
            dbModel.disconnect();
        } catch (Exception e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
        }
    }

    private static void displayEvaluation(DatabaseModel dbModel) {
        System.out.println("Evaluation Summary:");
        if (dbModel.doesUserExist("testUser"))
            System.out.println("User exists - Satisfied");
        else
            System.out.println("User exists - Not Satisfied");

        if (dbModel.isUserAdmin("testUser"))
            System.out.println("User is Admin - Satisfied");
        else
            System.out.println("User is Admin - Not Satisfied");

        if (dbModel.validateOneTimeCode("testUser", "validCode"))
            System.out.println("One-time code validation - Satisfied");
        else
            System.out.println("One-time code validation - Not Satisfied");
    }
}