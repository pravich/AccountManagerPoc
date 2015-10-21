package com.yggdrasil.europa.account.directory.model;

public class UserDirectoryEntity {
	public String userId;			// uid - use accountId (integer) as a uid in directory
	public String username;			// cn
	public String password;			// userPassword
	public String email;			// mail
	
	public String firstname;		// gn, givenName
	public String lastname;			// sn
	
	//public String company;			// o, organizationName
	public String role;				// title
	
	//public String mobile;			// mobile
	//public String homePhone;		// homePhone
	//public String officePhone;		// telephoneNumber
	//public String officeAddress;	// postalAddress
	//public String city;				// l
	//public String zipcode;			// postalCode
	
	public String description;		// description
}
