package com.yggdrasil.europa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.database.UserDatabase;
import com.yggdrasil.europa.account.database.UserDatabaseFactory;
import com.yggdrasil.europa.account.database.model.UserDatabaseEntity;

public class Main {
	private static Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		UserDatabaseEntity udb = new UserDatabaseEntity();
		udb.firstName = "Test01";
		udb.lastName  = "xxxxxx";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			udb.dateOfBirth = dateFormat.parse("1973-11-29");
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		udb.citizenId = "0123456789012";
		udb.citizenIdType = "TH-ID";
		udb.mobileNumber = "081-100-0000";
		udb.email = "test01@yggdrasil.com";
		udb.type = 1;
		udb.status = "INIT";
		
		UserDatabase db = UserDatabaseFactory.getUserDatabase();
		
		if (db.CreateUser(udb)) {
			logger.debug("create a new user successfully");
		} else {
			logger.debug("create a new user failed");
		}
	}
}
