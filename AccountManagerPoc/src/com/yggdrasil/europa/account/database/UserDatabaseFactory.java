package com.yggdrasil.europa.account.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.config.Configuration;
import com.yggdrasil.europa.account.database.exceptions.DatabaseConnectionException;

public class UserDatabaseFactory {
	private static Logger logger = LogManager.getLogger(UserDatabaseFactory.class);
	
	private static boolean initialized = false;
	
	static{
		if(!initialized) {
			logger.debug("initilizing UserDatabaseFactory.");
			
			try {
				Class.forName(Configuration.dbJdbcDriver);
				initialized = true;
				logger.debug("UserDatabaseFactory is initialized successfully.");
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.debug(e,e);
				initialized = false;
				logger.debug("UserDatabaseFactory initialization is failed.");
			}
		}
	}

	public static UserDatabase getUserDatabase() throws DatabaseConnectionException {
		logger.debug("creating a new UserDatabase instant.");
		UserDatabase udb = new UserDatabase();
		logger.debug("New UserDatabase instant is created.");
		return(udb);
	}

}
