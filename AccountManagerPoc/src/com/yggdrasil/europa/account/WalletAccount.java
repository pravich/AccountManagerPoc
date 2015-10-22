package com.yggdrasil.europa.account;

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
import com.yggdrasil.europa.account.model.WalletAccountEntity;

public class WalletAccount {
	private static Logger logger = LogManager.getLogger(WalletAccount.class);
	
	public static String signon(String genericId, String password) {		
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
	
	public boolean createWalletAccount(WalletAccountEntity waEntity) {
		UserDatabaseEntity udbEntity;
		
		udbEntity = new UserDatabaseEntity();
		
		udbEntity.username = waEntity.username;
		udbEntity.firstname = waEntity.firstname;
		udbEntity.lastname = waEntity.lastname;
		udbEntity.middlename = waEntity.middlename;
		udbEntity.title = waEntity.title;
		udbEntity.dateOfBirth = waEntity.dateOfBirth;
		udbEntity.gender = waEntity.gender;
		udbEntity.citizenId = waEntity.citizenId;
		udbEntity.citizenIdType = waEntity.citizenIdType;
		udbEntity.mobile = waEntity.mobile;
		udbEntity.email = waEntity.email;
		udbEntity.alternateEmail = waEntity.alternateEmail;
		udbEntity.postalAddress = waEntity.postalAddress;
		udbEntity.city = waEntity.city;
		udbEntity.zipcode = waEntity.zipcode;
		udbEntity.country = waEntity.country;
		udbEntity.homePhone = waEntity.homePhone;
		udbEntity.company = waEntity.company;
		udbEntity.officeAddress = waEntity.officeAddress;
		udbEntity.officePhone = waEntity.officePhone;
		udbEntity.type = waEntity.type;
		udbEntity.status = waEntity.status;
		udbEntity.directoryRole = waEntity.directoryRole;	
		
		UserDatabase udb;
		
		try {
			udb = UserDatabaseFactory.getUserDatabase();
			
			waEntity.accountId = udb.createUser(udbEntity);
			
			if(waEntity.accountId <= 0) {
				logger.debug("create a new user in database failed.");
				return(false);
			} else {
				logger.debug("create a new user in database success");
			}
		} catch(DatabaseConnectionException e) {
			logger.error(e.getMessage());
			logger.debug(e, e);
			return false;
		}
		
		UserDirectoryEntity udirEntity;
		
		udirEntity = new UserDirectoryEntity();
		udirEntity.userId = Integer.toString(waEntity.accountId);
		udirEntity.username = waEntity.username;
		udirEntity.password = waEntity.password;
		udirEntity.email = waEntity.email;
		
		udirEntity.firstname = waEntity.firstname;
		udirEntity.lastname = waEntity.lastname;
		udirEntity.role = waEntity.role;
		udirEntity.description = waEntity.description;
		
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
			
			logger.debug("rollback account[" + waEntity.accountId + "] due to user creation failure in directory.");
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

	private static boolean isEmail(String genericId) {
		int atSignPosition = genericId.indexOf('@');
		
		if(atSignPosition > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean isMobile(String genericId) {
		char[] chMobile = genericId.toCharArray();
		for(int i = 0; i < chMobile.length; i++) {
			if(!((chMobile[i] >= '0') && (chMobile[i] <= '9'))) {
				return false;
			}
		}
		
		return true;
	}
}
