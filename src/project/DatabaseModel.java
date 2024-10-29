package project;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Array;

import project.DatabaseHelper;

public class DatabaseModel {
	
	public DatabaseModel() {
		
		
		
	}
	
	public void connect() {
		try {
			DatabaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean hasCode(String code) {
		return DatabaseHelper.doesExist("codes", "code", code);
	}
	
	/**
	 * Register a user
	 * @param username The username of the user
	 * @param password The password of the user
	 * @param email The user's email
	 * @param roles The roles assigned to the user
	 * @param onetime Flag indicating one time password
	 * @param date Expiration date on one time password in milliseconds since 1970/1/1 (UTC)
	 * @param name full name
	 */
	public void registerUser(
			String username, 
			String password, 
			String email, 
			Object[] roles, 
			boolean onetime, 
			OffsetDateTime date, // in ms since 1970/1/1
			String[] name) 
	{
		
		try {
			DatabaseHelper.register(
					username,
					password,
					email,
					roles,
					onetime,
					date,
					name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * register a new code into the database
	 * @param code The code to be registered
	 * @param roles The roles associated with this code
	 */
	public void registerCode(String code, String... roles) {
		try {
			DatabaseHelper.registerCode(code, roles);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * Only displays user name, full name, and roles of each user
     * For use by admin only
     * @throws SQLException
     */
    public static void displayUsersbyAdmin() throws SQLException {
    	try {
    		DatabaseHelper.displayUsersbyAdmin();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
	 * Edit a value within the user table
	 * @param username Username of the user
	 * @param field Field being changed
	 * @param newValue The new value
	 */
	public void editUser(String username, String field, Object newValue) {
		try {
			DatabaseHelper.update("users", field, "username", username, newValue);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addCodeRoles(String code, String... roles) {
		Object[] r = getCodeRoles(code);
		Set<String> set = new HashSet<>();
		
		for(Object o : r) set.add((String) o);
		for(String s : roles) set.add(s);
		
		try {
			DatabaseHelper.update("codes", "roles", "code", code, set.toArray());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the value of a user field
	 * @param username username of user
	 * @param field Field being retrieved
	 * @return the value of the field for username
	 */
	public Object getUserField(String username, String field) {
		try {
			return DatabaseHelper.getValue("users", "username", username, field);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Object[] getCodeRoles(String code) {
		try {
			return (Object[]) ((Array) DatabaseHelper.getValue("codes", "code", code, "roles")).getArray();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Resets the user's password and gives them a new, one time password that will
	 * expire 1 week from the date of the reset.
	 * @param username The user name of the user to be reset
	 * @throws SQLException
	 */
	public void resetUser(String username) {
		// there are a few things that have to be set, so this function is called for each
		// This figures out the time in UTC that the reset function is called
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		
		this.editUser(username, "onetime", true);
		this.editUser(username, "password", null); // TODO: change from null to a one-time code
		this.editUser(username, "date", currentTime.plusWeeks(1)); // Adds a week to the current date
	}
	
	/**
	 * Add a new user with specified roles.
	 * 
	 * @param username The username of the new user
	 * @param roles An array of roles assigned to the user
	 * @throws SQLException if a database access error occurs
	 */
	public void addUser(String username, String[] roles) {
	    try {
	        DatabaseHelper.addUser(username, roles);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void removeUser(String username) {
		try {
			DatabaseHelper.remove("users", "username", username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeCode(String code) {
		try {
			DatabaseHelper.remove("codes", "code", code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
 	/**
	 * Drop the codes table
	 */
	public void dropCodesTable() {
		try {
			DatabaseHelper.dropTable("codes");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Drop the user table
	 */
	public void dropUserTable() {
		try {
			DatabaseHelper.dropTable("users");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * reset the user table
	 */
	public void resetUserTable() {
		dropUserTable();
		try {
			DatabaseHelper.createTables();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * reset the codes table
	 */
	public void resetCodeTable() {
		dropCodesTable();
		try {
			DatabaseHelper.createTables();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * reset all tables
	 */
	public void resetTables() {
		resetUserTable();
		resetCodeTable();
	}
}




