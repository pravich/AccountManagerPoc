package com.yggdrasil.europa.account.database.model;

import java.util.Date;

public class UserDatabaseEntity {
	// Account Attributes
	public int 		AccountId; // aid
	public String 	firstName;
	public String 	lastName;
	public String 	middleName;
	public String 	title;
	public Date 	dateOfBirth;
	public String 	gender;
	public String 	citizenId;
	public String 	citizenIdType;
	public String 	mobileNumber;
	public String 	email;
	public String 	alternateEmail;
	public String 	postalAddress;
	public String 	city;
	public String 	zipcode;
	public String 	country;
	public int 		type;
	public String 	status;
	public String 	directoryRole;
	public Date 	modificationDate;
	public Date 	creationDate;
}
