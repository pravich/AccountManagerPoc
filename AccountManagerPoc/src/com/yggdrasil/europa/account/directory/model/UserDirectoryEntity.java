package com.yggdrasil.europa.account.directory.model;

public class UserDirectoryEntity {
	public String userId;			// uid - use accountId (integer) as a uid in directory
	public String username;			// cn
	public String password;			// userPassword
	public String email;			// mail
	
	public String firstname;		// gn, givenName
	public String lastname;			// sn
	public String role;				// title
	public String description;		// description
}
