package com.yggdrasil.europa.account;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.cache.WalletAccountCache;
import com.yggdrasil.europa.account.cache.WalletAccountCacheItem;
import com.yggdrasil.europa.account.database.UserDatabase;
import com.yggdrasil.europa.account.database.UserDatabaseFactory;
import com.yggdrasil.europa.account.database.exceptions.DatabaseConnectionException;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;
import com.yggdrasil.europa.account.directory.UserDirectory;
import com.yggdrasil.europa.account.directory.UserDirectoryFactory;
import com.yggdrasil.europa.account.directory.exceptions.DirectoryConnectionException;
import com.yggdrasil.europa.account.directory.exceptions.InvalidCredentialException;
import com.yggdrasil.europa.account.directory.model.UserDirectoryEntity;

public class WalletAccount {
	private static Logger logger = LogManager.getLogger(WalletAccount.class);
	
	private UserDirectoryEntity udirEntity;
	private UserDatabaseEntity udbEntity;
	
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
		int accountId = -1; 
		String sessionToken = null;
		
		try {
			UserDatabase udb = UserDatabaseFactory.getUserDatabase();
			
			if(isEmail(genericId)) {
				accountId = udb.getAccountIdByEmail(genericId);
				logger.debug("signon with email=[" + genericId + "] matches to accountId=[" + accountId + "]");
			} else if(isMobile(genericId)) {
				accountId = udb.getAccountIdByMobile(genericId);
				logger.debug("signon with mobile=[" + genericId + "] matches to accountId=[" + accountId + "]");
			} else {
				accountId = udb.getAccountIdByUsername(genericId);
				logger.debug("signon with username=[" + genericId + "] matches to accountId=[" + accountId + "]");
			}
		} catch (DatabaseConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return null;
		}

		if(accountId < 0) {
			return null;
		}
		
		try {
			String uid = Integer.toString(accountId);
			
			if(UserDirectory.signon(uid, password)) {
				WalletAccountCacheItem cacheItem = new WalletAccountCacheItem();
				cacheItem.accountId = accountId;
			
				UserDirectory udir = UserDirectoryFactory.getUserDirectory();
				cacheItem.role = udir.getUserRole(uid);
				udir.close();
				
				sessionToken = SessionTokenGenerator.getSessionToken();
				WalletAccountCache.setSessionToken(sessionToken, cacheItem);
				
				logger.debug("accountId=[" + accountId + "] signon success");
			}
		} catch (DirectoryConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return null;			
		} catch (InvalidCredentialException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return null;			
		}
		
		return sessionToken;
	}
	
	public boolean createWalletAccount() {
		udbEntity = new UserDatabaseEntity();
		udbEntity.username = this.username;
		udbEntity.firstname = this.firstname;
		udbEntity.lastname = this.lastname;
		udbEntity.middlename = this.middlename;
		udbEntity.title = this.title;
		udbEntity.dateOfBirth = this.dateOfBirth;
		udbEntity.gender = this.gender;
		udbEntity.citizenId = this.citizenId;
		udbEntity.citizenIdType = this.citizenIdType;
		udbEntity.mobile = this.mobile;
		udbEntity.email = this.email;
		udbEntity.alternateEmail = this.alternateEmail;
		udbEntity.postalAddress = this.postalAddress;
		udbEntity.city = this.city;
		udbEntity.zipcode = this.zipcode;
		udbEntity.country = this.country;
		udbEntity.homePhone = this.homePhone;
		udbEntity.company = this.company;
		udbEntity.officeAddress = this.officeAddress;
		udbEntity.officePhone = this.officePhone;
		udbEntity.type = this.type;
		udbEntity.status = this.status;
		udbEntity.directoryRole = this.directoryRole;	
		
		UserDatabase udb;
		
		try {
			udb = UserDatabaseFactory.getUserDatabase();
		} catch(DatabaseConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		accountId = udb.createUser(udbEntity);
		
		if(accountId <= 0) {
			logger.debug("create a new user in database failed.");
			return(false);
		} else {
			logger.debug("create a new user in database success");
		}
		
		udirEntity = new UserDirectoryEntity();
		udirEntity.userId = Integer.toString(accountId);
		udirEntity.username = username;
		udirEntity.password = password;
		udirEntity.email = email;
		
		udirEntity.firstname = this.firstname;
		udirEntity.lastname = this.lastname;
		udirEntity.role = this.role;
		udirEntity.description = this.description;
		
		UserDirectory udir;
		
		try {
			udir = UserDirectoryFactory.getUserDirectory();
		} catch (DirectoryConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		if(!udir.createUser(udirEntity)) {
			// delete user from database
			//logger.debug("remove account[" + accountId + "] from database due to user creation failure in directory.");
			//udb.deleteUser(accountId);
			
			logger.debug("rollback account[" + accountId + "] due to user creation failure in directory.");
			udb.rollback();
			return false;
		}
		
		// Commit pending user creation in database
		udb.commit();
		udb.close();
		return true;
	}
	
	public boolean changeCapSet() {
		// verify session token and role
		return false;
	}

	protected boolean isEmail(String genericId) {
		int atSignPosition = genericId.indexOf('@');
		
		if(atSignPosition > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean isMobile(String genericId) {
		char[] chMobile = genericId.toCharArray();
		for(int i = 0; i < chMobile.length; i++) {
			if(!((chMobile[i] >= '0') && (chMobile[i] <= '9'))) {
				return false;
			}
		}
		
		return true;
	}
	
	/************************************************************************/
	
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
