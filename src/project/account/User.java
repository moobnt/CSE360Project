package project.account;

import java.util.Date;

/***
 * 
 * <p> user - data storage. </p>
 * 
 * <p> Description: A storage for user data, collected from user input. The code is
 * instantiated holders for the user data.
 * </p>
 * 
 * 
 * 
 * @author Group TH 58
 * 
 * @version 1.00		2024-10-9	Initial baseline
 * 
 */

public class User {
	public String username;
	public String password;
	public String email;
	public Object[] roles;
	public boolean oneTime;
	public Date oneTimeDate;
	public FullName fullName;
	
	// No-argument constructor
    public User() {}
	
	// Constructor accepting username, password, and roles
    public User(String username, String password, String[] roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}

class FullName {
	public String first;
	public String middle;
	public String last;
	public String preferred;
}
