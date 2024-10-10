package project;

import java.util.Date;

public class User {
	public String username;
	public String password;
	public String email;
	public Object[] roles;
	public boolean oneTime;
	public Date oneTimeDate;
	public FullName fullName;
	
}

class FullName {
	public String first;
	public String middle;
	public String last;
	public String preferred;
}