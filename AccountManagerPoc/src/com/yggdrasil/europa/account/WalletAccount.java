package com.yggdrasil.europa.account;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.database.UserDatabase;
import com.yggdrasil.europa.account.database.UserDatabaseFactory;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;
import com.yggdrasil.europa.account.directory.UserDirectory;
import com.yggdrasil.europa.account.directory.UserDirectoryFactory;
import com.yggdrasil.europa.account.directory.exceptions.ConnectionException;
import com.yggdrasil.europa.account.directory.model.UserDirectoryEntity;

public class WalletAccount {
	private static Logger logger = LogManager.getLogger(WalletAccount.class);
	
	private UserDirectoryEntity udir;
	private UserDatabaseEntity udb;
	
	// Key Attributes
	private int 	accountId;
	private String 	username; 
	private String 	email;
	private String 	mobile;
	public String 	password;		// userPassword
	
	// Directory Attributes
	private String 	firstname;		// gn, givenName
	private String 	lastname;		// sn
	private String 	role;			// title
	private String 	description;	// description
	
	
	// Database Attributes
//	private String 	firstname;
//	private String 	lastname;
	private String 	middlename;
	private String 	title;
	private Date 	dateOfBirth;
	private String 	gender;
	private String 	citizenId;
	private String 	citizenIdType;
//	private String 	mobile;
//	private String 	email;
	private String 	alternateEmail;
	private String 	postalAddress;
	private String 	city;
	private String 	zipcode;
	private String 	country;
	private String 	homePhone;
	private String 	company;
	private String 	officeAddress;
	private String 	officePhone;
	private int 	type;
	private String 	status;
	private String 	directoryRole;
	private Date 	modificationDate;
	private Date 	creationDate;
	
	public String signon(String genericId, String password) {
		// identify what genericId is and convert to username.
		// if signon success, return session token
		// if signon success, retrieve account from both directory and database
		
		return null;
	}
	
	public boolean createWalletAccount() {
		udb = new UserDatabaseEntity();
		udb.username = this.username;
		udb.firstname = this.firstname;
		udb.lastname = this.lastname;
		udb.middlename = this.middlename;
		udb.title = this.title;
		udb.dateOfBirth = this.dateOfBirth;
		udb.gender = this.gender;
		udb.citizenId = this.citizenId;
		udb.citizenIdType = this.citizenIdType;
		udb.mobile = this.mobile;
		udb.email = this.email;
		udb.alternateEmail = this.alternateEmail;
		udb.postalAddress = this.postalAddress;
		udb.city = this.city;
		udb.zipcode = this.zipcode;
		udb.country = this.country;
		udb.homePhone = this.homePhone;
		udb.company = this.company;
		udb.officeAddress = this.officeAddress;
		udb.officePhone = this.officePhone;
		udb.type = this.type;
		udb.status = this.status;
		udb.directoryRole = this.directoryRole;	
		
		UserDatabase db = UserDatabaseFactory.getUserDatabase();
		if(!db.CreateUser(udb)) {
			return(false);
		}
		
		accountId = udb.accountId;
		
		udir = new UserDirectoryEntity();
		udir.userId = Integer.toString(accountId);
		udir.username = username;
		udir.password = password;
		udir.email = email;
		
		udir.firstname = this.firstname;
		udir.lastname = this.lastname;
		udir.role = this.role;
		udir.description = this.description;
		
		UserDirectory ud;
		
		try {
			ud = UserDirectoryFactory.getUserDirectory();
		} catch (ConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		if(!ud.createUser(udir)) {
			
			// delete user from database
			return false;
		}
		
		return true;
	}
	
	public boolean changeCapSet() {
		// verify session token and role
		return false;
	}
	
	protected boolean verifyEmailFormat(String email) {
		int atSignPosition = email.indexOf('@');
		
		// if '@' at 1st or last position or no '@', return false.
		if (atSignPosition <= 0 || atSignPosition == (email.length() - 1)) {
			return false;
		}
		
		// if there are '@' more than one, return false.
		if(email.substring(atSignPosition).indexOf('@') >= 0) {
			return false;
		}
		
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getCitizenIdType() {
		return citizenIdType;
	}

	public void setCitizenIdType(String citizenIdType) {
		this.citizenIdType = citizenIdType;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDirectoryRole() {
		return directoryRole;
	}

	public void setDirectoryRole(String directoryRole) {
		this.directoryRole = directoryRole;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
}
