package com.yggdrasil.europa.account.model;

import java.util.Date;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WalletAccountEntity {
	
	private static Logger logger = LogManager.getLogger(WalletAccountEntity.class);
	
	// Key Attributes
	public int 		accountId;
	public String 	username; 
	public String 	email;			// mail
	public String 	mobile;
	public String 	password;		// userPassword
	
	// Directory Attributes
	public String 	firstname;		// gn, givenName
	public String 	lastname;		// sn
	public String 	role;			// title
	public String 	description;	// description
	
	
	// Database Attributes
//	public String 	firstname;
//	public String 	lastname;
	public String 	middlename;
	public String 	title;
	public Date 	dateOfBirth;
	public String 	gender;
	public String 	citizenId;
	public String 	citizenIdType;
//	public String 	mobile;
//	public String 	email;
	public String 	alternateEmail;
	public String 	postalAddress;
	public String 	city;
	public String 	zipcode;
	public String 	country;
	public String 	homePhone;
	public String 	company;
	public String 	officeAddress;
	public String 	officePhone;
	public int 		type;
	public String 	status;
	public String 	directoryRole;
	public Date 	modificationDate;
	public Date 	creationDate;
	
	public void log() {
		// write all attributes to log file.
	}
	
	public void load(Hashtable<String, String> updateList) {
		
	}
}
